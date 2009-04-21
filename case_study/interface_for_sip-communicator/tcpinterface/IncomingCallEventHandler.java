package net.java.sip.communicator.impl.tcpinterface;

import net.java.sip.communicator.impl.gui.main.call.CallManager;
import net.java.sip.communicator.service.protocol.CallState;
import net.java.sip.communicator.service.protocol.event.CallChangeEvent;
import net.java.sip.communicator.service.protocol.event.CallChangeListener;
import net.java.sip.communicator.service.protocol.event.CallParticipantEvent;

public class IncomingCallEventHandler implements CallChangeListener {

	private TcpInterfaceServiceImpl tcpServiceImpl = null;

	public IncomingCallEventHandler(TcpInterfaceServiceImpl tcpServiceImpl) {
		this.tcpServiceImpl = tcpServiceImpl;

	}

	// handle incoming call events
	public void callParticipantAdded(CallParticipantEvent evt) {
		// TODO Auto-generated method stub

	}

	public void callParticipantRemoved(CallParticipantEvent evt) {
		// TODO Auto-generated method stub

	}

	public void callStateChanged(CallChangeEvent evt) {
		
		if (evt.getNewValue() == CallState.CALL_IN_PROGRESS) {
			tcpServiceImpl.sendMessage("SESSION_CONNECTED");
		} else if (evt.getNewValue() == CallState.CALL_ENDED) {
			
			tcpServiceImpl.sendMessage("SESSION_DISCONNECTED");
			if (tcpServiceImpl.getInComingCallDialog() != null)
				CallManager.disposeCallDialogWait(tcpServiceImpl
						.getInComingCallDialog());
			
            // clean the call variables
			tcpServiceImpl.cleanCallVariables();
		}
	}

}
