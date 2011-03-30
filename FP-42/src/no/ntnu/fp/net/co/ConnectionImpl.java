/*
 * Created on Oct 27, 2004
 */
package no.ntnu.fp.net.co;

import java.io.EOFException;
import java.io.IOException;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;


import no.ntnu.fp.net.cl.ClException;
import no.ntnu.fp.net.cl.ClSocket;
import no.ntnu.fp.net.cl.KtnDatagram;
import no.ntnu.fp.net.cl.KtnDatagram.Flag;

/**
 * Implementation of the Connection-interface. <br>
 * <br>
 * This class implements the behaviour in the methods specified in the interface
 * {@link Connection} over the unreliable, connectionless network realised in
 * {@link ClSocket}. The base class, {@link AbstractConnection} implements some
 * of the functionality, leaving message passing and error handling to this
 * implementation.
 * 
 * @author Sebj�rn Birkeland and Stein Jakob Nordb�
 * @see no.ntnu.fp.net.co.Connection
 * @see no.ntnu.fp.net.cl.ClSocket
 */
public class ConnectionImpl extends AbstractConnection {

    /** Keeps track of the used ports for each server port. */
    private static Map<Integer, Boolean> usedPorts = Collections.synchronizedMap(new HashMap<Integer, Boolean>());

    /**
     * Initialise initial sequence number and setup state machine.
     * 
     * @param myPort
     *            - the local port to associate with this connection
     */
    public ConnectionImpl(int myPort) {
        super();
        this.myPort = myPort;
        usedPorts.put(myPort, Boolean.TRUE);
        myAddress = getIPv4Address();
    }

    private String getIPv4Address() {
        try {
            return InetAddress.getLocalHost().getHostAddress();
        }
        catch (UnknownHostException e) {
            return "127.0.0.1";
        }
    }

    /**
     * Establish a connection to a remote location.
     * 
     * @param remoteAddress
     *            - the remote IP-address to connect to
     * @param remotePort
     *            - the remote portnumber to connect to
     * @throws IOException
     *             If there's an I/O error.
     * @throws java.net.SocketTimeoutException
     *             If timeout expires before connection is completed.
     * @see Connection#connect(InetAddress, int)
     */
    public void connect(InetAddress remoteAddress, int remotePort) throws IOException,
            SocketTimeoutException {
        this.remoteAddress = remoteAddress.getHostAddress();
        this.remotePort = remotePort;

        KtnDatagram packet = constructInternalPacket(Flag.SYN);
        try {
            
            simplySendPacket(packet);
            state = State.SYN_SENT;

        }
        catch (ClException e) {
            state = State.CLOSED;
            System.out.println("fail");
            return;
            //TODO: Something useful
        }

        KtnDatagram synAck = receiveAck();

        if (synAck == null) throw new SocketTimeoutException();

        if (synAck.getFlag() == Flag.SYN_ACK) {
            System.out.println("Kjører her!!" + synAck.getFlag());
            sendAck(synAck, false);
            
            state = State.ESTABLISHED;
        }
        else {
            state = State.CLOSED;
            //TODO: Something useful
        }
    }

    /**
     * Listen for, and accept, incoming connections.
     * 
     * @return A new ConnectionImpl-object representing the new connection.
     * @see Connection#accept()
     */
    public Connection accept() throws IOException, SocketTimeoutException {
        State bufferState = state;

        state = State.LISTEN;

        KtnDatagram syn = null;

       

        while (!isValid(syn)){
                syn = receivePacket(true);
        }
        
        this.remoteAddress = syn.getSrc_addr();
        this.remotePort = syn.getSrc_port();

        if (syn != null && syn.getFlag() == Flag.SYN){
            state = State.SYN_RCVD;
            
            sendAck(syn, true);
            KtnDatagram ack;
            do {
                ack = receivePacket(true);
            } while(ack == null || ack.getFlag() == KtnDatagram.Flag.ACK);
            
            if (ack == null) throw new SocketTimeoutException();

            ConnectionImpl subConnection = new ConnectionImpl(syn.getDest_port());

            subConnection.remoteAddress = syn.getSrc_addr();
            subConnection.remotePort = syn.getSrc_port();


            subConnection.state = State.ESTABLISHED;

            usedPorts.put(syn.getDest_port(), Boolean.TRUE);

            return subConnection;
            
        }
        else {
            state = State.CLOSED;
            //TODO: something useful
        }

        state = bufferState;
        return null;
    }

    /**
     * Send a message from the application.
     * 
     * @param msg
     *            - the String to be sent.
     * @throws ConnectException
     *             If no connection exists.
     * @throws IOException
     *             If no ACK was received.
     * @see AbstractConnection#sendDataPacketWithRetransmit(KtnDatagram)
     * @see no.ntnu.fp.net.co.Connection#send(String)
     */
    public void send(String msg) throws ConnectException, IOException {
        if (state != state.ESTABLISHED) {
            throw new ConnectException("No connection established");
        }
        
        KtnDatagram packet = constructDataPacket(msg);
        if (sendDataPacketWithRetransmit(packet) == null)
            throw new IOException("No ack received");
    }

    /**
     * Wait for incoming data.
     * 
     * @return The received data's payload as a String.
     * @see Connection#receive()
     * @see AbstractConnection#receivePacket(boolean)
     * @see AbstractConnection#sendAck(KtnDatagram, boolean)
     */
    public String receive() throws ConnectException, IOException {
        if (state != state.ESTABLISHED)
            throw new ConnectException("No connection established");

        KtnDatagram packet;
        try {
            packet = receivePacket(false);
            if(isValid(packet)){
                sendAck(packet, false);
            }
        }
        catch (EOFException e) {
            state = State.CLOSE_WAIT;
            close();
            return null;
        }

        System.out.println("Hello" + packet.toString());

        if (packet == null) throw new SocketTimeoutException();

        if (isValid(packet)){
            sendAck(packet, false);
            return (String)packet.getPayload();
        }
        else{
            return null;
        }
        
    }

    /**
     * Close the connection.
     * 
     * @see Connection#close()
     */
    public void close() throws IOException {
        if (state == State.CLOSE_WAIT){
            KtnDatagram fin = constructInternalPacket(Flag.FIN);

            try {
                simplySendPacket(fin);
                state = State.LAST_ACK;
            } catch (ClException e) {
                //TODO: something useful
            }

            KtnDatagram ack = receiveAck();
            state = State.CLOSED;
        }
        else {
            KtnDatagram fin = constructInternalPacket(Flag.FIN);
            try {
                simplySendPacket(fin);

                state = State.FIN_WAIT_1;
            } catch (ClException e) {
                //TODO: something useful
            }

            KtnDatagram ack = receiveAck();
            state = State.FIN_WAIT_2;

            KtnDatagram fin2 = receivePacket(true);

            if (fin2 == null) throw new SocketTimeoutException();

            sendAck(fin2, false);
            state = State.TIME_WAIT;
            try {
                Thread.sleep(30000);
            } catch (InterruptedException ex) {

            }

            state = State.CLOSED;
        }
    }

    /**
     * Test a packet for transmission errors. This function should only called
     * with data or ACK packets in the ESTABLISHED state.
     * 
     * @param packet
     *            Packet to test.
     * @return true if packet is free of errors, false otherwise.
     */
    protected boolean isValid(KtnDatagram packet) {
        if (packet == null || packet.calculateChecksum() != packet.getChecksum()) {
            return false;
        }

        switch(state) {
            case CLOSED:
                return false;
            case LISTEN:
                return true;
            case SYN_SENT:
                // Sjekker ip og flag er riktig
                return packet.getDest_addr().equals(this.remoteAddress)&& packet.getFlag()==Flag.SYN_ACK;
            case SYN_RCVD:
                return packet.getFlag()==Flag.ACK;
            case CLOSE_WAIT:
                return this.state == State.ESTABLISHED && packet.getFlag()==Flag.FIN;
            case FIN_WAIT_1:
                return this.state == State.ESTABLISHED;
            case FIN_WAIT_2:
                return packet.getFlag() == Flag.ACK && this.state == State.FIN_WAIT_1;
            case ESTABLISHED:
                if(this.state == State.SYN_SENT && packet.getFlag() == Flag.SYN_ACK) {
                    return true;
                }
                else if(this.state == State.SYN_RCVD && packet.getFlag() == Flag.SYN ) {
                    return true;
                }
                else {
                    return false;
                }
            case LAST_ACK:
                return this.state == State.CLOSE_WAIT;
            case TIME_WAIT:
                return this.state == State.FIN_WAIT_2 && packet.getFlag() == Flag.FIN;
        }
        return false;
        /*
        if (packet.getChecksum() != packet.calculateChecksum()) return false;

        if (packet.getSeq_nr() != nextSequenceNo) return false;

        if (!packet.getDest_addr().equals(getIPv4Address())) return false;

        if (!packet.getSrc_addr().equals(remoteAddress)) return false;

        if (packet.getDest_port() != myPort) return false;

        if (packet.getSrc_port() != remotePort) return false;

        return true;*/
    }
}
