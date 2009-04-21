package net.java.sip.communicator.impl.tcpinterface;

import net.java.sip.communicator.service.protocol.CallParticipantState;
import net.java.sip.communicator.service.protocol.event.CallParticipantChangeEvent;
import net.java.sip.communicator.service.protocol.event.CallParticipantListener;

public class OutgoingCallEventHandler implements CallParticipantListener {

	private TcpInterfaceServiceImpl tcpServiceImpl = null;

	// handle outgoing call events

	public OutgoingCallEventHandler(TcpInterfaceServiceImpl tcpServiceImpl) {
		this.tcpServiceImpl = tcpServiceImpl;

	}

	public void participantAddressChanged(CallParticipantChangeEvent evt) {
		// TODO Auto-generated method stub

	}

	public void participantDisplayNameChanged(CallParticipantChangeEvent evt) {
		// TODO Auto-generated method stub

	}

	public void participantImageChanged(CallParticipantChangeEvent evt) {
		// TODO Auto-generated method stub

	}

	public void participantStateChanged(CallParticipantChangeEvent evt) {

		if (evt.getNewValue() == CallParticipantState.ALERTING_REMOTE_SIDE) {
			tcpServiceImpl.sendMessage("RINGING");
		}
		if (evt.getNewValue() == CallParticipantState.CONNECTING_WITH_EARLY_MEDIA) {
			tcpServiceImpl.sendMessage("SESSION_IN_PROGRESS");
		} else if (evt.getNewValue() == CallParticipantState.CONNECTING) {
			tcpServiceImpl.sendMessage("TRYING");
		} else if (evt.getNewValue() == CallParticipantState.CONNECTED) {
			tcpServiceImpl.sendMessage("SESSION_CONNECTED");
		} else if (evt.getNewValue() == CallParticipantState.INCOMING_CALL) {
			// tcpServiceImpl.sendMessage("INCOMING_CALL");
		} else if (evt.getNewValue() == CallParticipantState.INITIATING_CALL) {
			// tcpServiceImpl.sendMessage("INITIATING_CALL");
		} else if (evt.getNewValue() == CallParticipantState.DISCONNECTED) {
			tcpServiceImpl.sendMessage("SESSION_DISCONNECTED");
			tcpServiceImpl.setOutGoingCall(null);
		} else if (evt.getNewValue() == CallParticipantState.FAILED) {
			tcpServiceImpl.sendMessage("SESSION_FAILED");
			tcpServiceImpl.setOutGoingCall(null);
		} else if (evt.getNewValue() == CallParticipantState.BUSY) {
			tcpServiceImpl.sendMessage("BUSY_HERE");
			tcpServiceImpl.setOutGoingCall(null);
		}
	}

	public void participantTransportAddressChanged(
			CallParticipantChangeEvent evt) {
		// TODO Auto-generated method stub

	}

}
