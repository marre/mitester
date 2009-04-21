package net.java.sip.communicator.impl.tcpinterface;

import java.util.concurrent.TimeUnit;

import net.java.sip.communicator.service.protocol.RegistrationState;
import net.java.sip.communicator.service.protocol.event.RegistrationStateChangeEvent;
import net.java.sip.communicator.service.protocol.event.RegistrationStateChangeListener;

public class RegisterEventHandler implements RegistrationStateChangeListener {

	private TcpInterfaceServiceImpl tcpServiceImpl = null;

	public RegisterEventHandler(TcpInterfaceServiceImpl tcpServiceImpl) {
		this.tcpServiceImpl = tcpServiceImpl;

	}

	/**
	 * handle register events
	 */

	public void registrationStateChanged(RegistrationStateChangeEvent evt) {
		if (evt.getNewState().equals(RegistrationState.REGISTERED)) {
			tcpServiceImpl.sendMessage("REGISTERED");

		} else if (evt.getNewState().equals(RegistrationState.UNREGISTERING)) {
			tcpServiceImpl.sendMessage("DE-REGISTERING");
		} else if (evt.getNewState().equals(RegistrationState.UNREGISTERED)) {

			if (tcpServiceImpl.getIsRegisterAll()) {

				tcpServiceImpl.setIsRegisterAll(false);

				// remove the account if exist
				tcpServiceImpl.removeAccount();

				// added short delay
				setDelay(200);

			}

			tcpServiceImpl.sendMessage("DE-REGISTERED");

		} else if (evt.getNewState().equals(RegistrationState.REGISTERING)) {
			tcpServiceImpl.sendMessage("REGISTERING");
		} else if (evt.getNewState().equals(
				RegistrationState.AUTHENTICATION_FAILED)
				|| evt.getNewState()
						.equals(RegistrationState.CONNECTION_FAILED)) {
			tcpServiceImpl.sendMessage("REGISTRATION_FAILED");
		}
	}

	/**
	 * set time delay
	 * 
	 * @param milliseconds
	 */

	private void setDelay(long milliseconds) {
		try {

			TimeUnit.MILLISECONDS.sleep(milliseconds);

		} catch (InterruptedException e) {

		}
	}

}
