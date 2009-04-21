package net.java.sip.communicator.impl.tcpinterface;

import net.java.sip.communicator.service.protocol.Call;
import net.java.sip.communicator.service.protocol.event.CallEvent;
import net.java.sip.communicator.service.protocol.event.CallListener;

public class CallEventHandler implements CallListener {

	private TcpInterfaceServiceImpl tcpServiceImpl = null;

	private Call incomingcall = null;

	public CallEventHandler(TcpInterfaceServiceImpl tcpServiceImpl) {
		this.tcpServiceImpl = tcpServiceImpl;

	}

	// handle all call events both incoming and outgoing

	public void callEnded(CallEvent event) {
		// TODO Auto-generated method stub
	}

	public void incomingCallReceived(CallEvent event) {
		incomingcall = event.getSourceCall();
		tcpServiceImpl.setInComingCall(incomingcall);
		tcpServiceImpl.sendMessage("INVITE");
	}

	public void outgoingCallCreated(CallEvent event) {
		tcpServiceImpl.sendMessage("INITIATING_CALL");

	}

}
