package net.java.sip.communicator.impl.tcpinterface;

import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.SocketException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Map.Entry;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import net.java.sip.communicator.impl.gui.GuiActivator;
import net.java.sip.communicator.impl.gui.main.call.CallDialog;
import net.java.sip.communicator.impl.gui.main.call.CallManager;
import net.java.sip.communicator.impl.gui.main.call.CallPanel;
import net.java.sip.communicator.impl.gui.main.call.GuiCallParticipantRecord;
import net.java.sip.communicator.plugin.sipaccregwizz.SIPAccountRegistrationWizard;
import net.java.sip.communicator.service.configuration.ConfigurationService;
import net.java.sip.communicator.service.gui.AccountRegistrationWizard;
import net.java.sip.communicator.service.protocol.AccountID;
import net.java.sip.communicator.service.protocol.Call;
import net.java.sip.communicator.service.protocol.CallParticipant;
import net.java.sip.communicator.service.protocol.CallParticipantState;
import net.java.sip.communicator.service.protocol.CallState;
import net.java.sip.communicator.service.protocol.OperationFailedException;
import net.java.sip.communicator.service.protocol.OperationSetBasicTelephony;
import net.java.sip.communicator.service.protocol.ProtocolProviderFactory;
import net.java.sip.communicator.service.protocol.ProtocolProviderService;
import net.java.sip.communicator.service.protocol.RegistrationState;
import net.java.sip.communicator.service.protocol.event.CallChangeEvent;
import net.java.sip.communicator.service.protocol.event.CallChangeListener;
import net.java.sip.communicator.service.protocol.event.CallEvent;
import net.java.sip.communicator.service.protocol.event.CallListener;
import net.java.sip.communicator.service.protocol.event.CallParticipantChangeEvent;
import net.java.sip.communicator.service.protocol.event.CallParticipantEvent;
import net.java.sip.communicator.service.protocol.event.CallParticipantListener;
import net.java.sip.communicator.service.protocol.event.RegistrationStateChangeEvent;
import net.java.sip.communicator.service.protocol.event.RegistrationStateChangeListener;

import org.osgi.framework.ServiceReference;

public class TcpInterfaceServiceImpl implements Runnable,
		RegistrationStateChangeListener, CallChangeListener,
		CallParticipantListener, CallListener {

	private static final BufferedWriter SYSTEM_OUT = new BufferedWriter(
			new OutputStreamWriter(System.out));

	private static final BufferedWriter SYSTEM_ERR = new BufferedWriter(
			new OutputStreamWriter(System.err));

	private static final String REGISTERED = "REGISTERED";

	private static final String REGISTERING = "REGISTERING";

	private static final String REGISTRATION_FAILED = "REGISTRATION_FAILED";

	private static final String DE_REGISTERING = "DE-REGISTERING";

	private static final String DE_REGISTERED = "DE-REGISTERED";

	private static final String RINGING = "RINGING";

	private static final String SESSION_IN_PROGRESS = "SESSION_IN_PROGRESS";

	private static final String TRYING = "TRYING";

	private static final String SESSION_CONNECTED = "SESSION_CONNECTED";

	private static final String SESSION_DISCONNECTED = "SESSION_DISCONNECTED";

	private static final String SESSION_FAILED = "SESSION_FAILED";

	private static final String BUSY_HERE = "BUSY_HERE";

	private static final String INVITE = "INVITE";

	private static final String INITIATING_CALL = "INITIATING_CALL";

	private static final String HOST_NAME = "localhost";

	private static final int TCP_PORT = 7777;

	private ExecutorService clientExecutors = Executors.newFixedThreadPool(1);

	private ProtocolProviderService pps = null;

	private Call outgoingcall = null;

	private Call incomingcall = null;

	private CallDialog inComingCallDialog = null;

	private boolean isDeRegisterAll = false;

	private OperationSetBasicTelephony callService = null;

	private Socket clientSocket = null;

	private DataOutputStream dataOut = null;

	private DataInputStream dataIn = null;

	private volatile boolean done = false;

	public TcpInterfaceServiceImpl() {

	}

	public void tcpStartService() {
		clientExecutors.execute(this);
	}

	/**
	 * Events below are triggered on REGISTER transaction
	 * 
	 */
	public void registrationStateChanged(RegistrationStateChangeEvent evt) {

		if (evt.getNewState().equals(RegistrationState.REGISTERED)) {

			callService = (OperationSetBasicTelephony) TcpInterfaceActivator
					.getCreateCallService();

			if (callService != null) {
				System.out.println("call service is not null");
			} else {
				System.out.println("call service is null");
			}

			callService.addCallListener(this);

			// /* make a delay to sending notification */
			// try {
			//
			// TimeUnit.MILLISECONDS.sleep(100);
			//
			// } catch (InterruptedException e) {
			//
			// // TODO Auto-generated catch block
			// e.printStackTrace();
			// }

			/* send to miTester */
			sendMessage(REGISTERED);

			printMessage(REGISTERED);

		} else if (evt.getNewState().equals(RegistrationState.UNREGISTERING)) {

			/* send to miTester */
			sendMessage(DE_REGISTERING);

			printMessage(DE_REGISTERING);

		} else if (evt.getNewState().equals(RegistrationState.UNREGISTERED)) {

			/* make a delay to sending notification */
			if (isDeRegisterAll) {

				isDeRegisterAll = false;

				/* remove the account if exist */
				removeAccount();

				/* make a delay to sending notification */
				try {

					TimeUnit.MILLISECONDS.sleep(200);

				} catch (InterruptedException e) {

					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}

			/* send to miTester */
			sendMessage(DE_REGISTERED);

			printMessage(DE_REGISTERED);

		} else if (evt.getNewState().equals(RegistrationState.REGISTERING)) {
			/* send to miTester */
			sendMessage(REGISTERING);

			printMessage(REGISTERING);
		} else if (evt.getNewState().equals(
				RegistrationState.AUTHENTICATION_FAILED)
				|| evt.getNewState()
						.equals(RegistrationState.CONNECTION_FAILED)) {

			/* send to miTester */
			sendMessage(REGISTRATION_FAILED);

			printMessage(REGISTRATION_FAILED);
		}
	}

	/**
	 * Events below are triggered on Call transaction
	 * 
	 */

	public void callParticipantAdded(CallParticipantEvent evt) {
		// TODO Auto-generated method stub
	}

	public void callParticipantRemoved(CallParticipantEvent evt) {
		// TODO Auto-generated method stub
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
			/* send to miTester */
			sendMessage(RINGING);

			printMessage(RINGING);
		}
		if (evt.getNewValue() == CallParticipantState.CONNECTING_WITH_EARLY_MEDIA) {
			/* send to miTester */
			sendMessage(SESSION_IN_PROGRESS);

			printMessage(SESSION_IN_PROGRESS);

		} else if (evt.getNewValue() == CallParticipantState.CONNECTING) {
			/* send to miTester */
			sendMessage(TRYING);

			printMessage(TRYING);

		} else if (evt.getNewValue() == CallParticipantState.CONNECTED) {
			
			/* send to miTester */
			sendMessage(SESSION_CONNECTED);

			printMessage(SESSION_CONNECTED);

		} else if (evt.getNewValue() == CallParticipantState.INCOMING_CALL) {
			// sendMessage("INCOMING_CALL");
		} else if (evt.getNewValue() == CallParticipantState.INITIATING_CALL) {
			// sendMessage(INITIATING_CALL);
		} else if (evt.getNewValue() == CallParticipantState.DISCONNECTED) {
			/* send to miTester */
			sendMessage(SESSION_DISCONNECTED);

			printMessage(SESSION_DISCONNECTED);
			outgoingcall = null;
		} else if (evt.getNewValue() == CallParticipantState.FAILED) {
			/* send to miTester */
			sendMessage(SESSION_FAILED);
			printMessage(SESSION_FAILED);
			outgoingcall = null;
		} else if (evt.getNewValue() == CallParticipantState.BUSY) {
			/* send to miTester */
			sendMessage(BUSY_HERE);
			printMessage(BUSY_HERE);
			outgoingcall = null;
		}
	}

	public void participantTransportAddressChanged(
			CallParticipantChangeEvent evt) {
	}

	public void callStateChanged(CallChangeEvent evt) {

		if (evt.getNewValue() == CallState.CALL_IN_PROGRESS) {
			
			/* send to miTester */
			sendMessage(SESSION_CONNECTED);

			printMessage(SESSION_CONNECTED);

		} else if (evt.getNewValue() == CallState.CALL_ENDED) {

			/* send to miTester */
			sendMessage(SESSION_DISCONNECTED);

			printMessage(SESSION_DISCONNECTED);

			if (inComingCallDialog != null)
				CallManager.disposeCallDialogWait(inComingCallDialog);

			incomingcall = null;
			inComingCallDialog = null;
			outgoingcall = null;
		}
	}

	/**
	 * Events triggered on Call transaction
	 */
	public void callEnded(CallEvent event) {
	}

	public void incomingCallReceived(CallEvent event) {

		incomingcall = event.getSourceCall();

		incomingcall.addCallChangeListener(this);

		/* send to miTester */
		sendMessage(INVITE);

		printMessage("RECEVING_CALL");
	}

	public void outgoingCallCreated(CallEvent event) {

		/* send to miTester */
		sendMessage(INITIATING_CALL);

		printMessage(INITIATING_CALL);

	}

	/**
	 * register the sip communicator
	 * 
	 * @param userid
	 *            is an username to be registered at the server
	 * @param password
	 *            is a password
	 * @param keepAliveinterval
	 *            is an interval set for retransmitting the REGISTER request
	 * @param serverPort
	 *            is a server port
	 * @param clientPort
	 *            is a proxy port
	 */

	public void register(String userid, String password,
			String keepAliveinterval, String serverPort, String proxyPort) {
		try {
			AccountRegistrationWizard accRegWizzard = (AccountRegistrationWizard) TcpInterfaceActivator
					.getAccountRegistrationWizardService();
			SIPAccountRegistrationWizard sipaccRegWizzard = (SIPAccountRegistrationWizard) accRegWizzard;
			sipaccRegWizzard.getRegistration().setKeepAliveInterval(
					keepAliveinterval);
			sipaccRegWizzard.getRegistration().setServerPort(serverPort);
			sipaccRegWizzard.getRegistration().setProxyPort(proxyPort);

			pps = accRegWizzard.signin(userid, password);

			pps.addRegistrationStateChangeListener(this);

		} catch (OperationFailedException ex) {

			sendMessage("registration failed");
			printError("registration failed", ex);
		} catch (Exception ex) {
			sendMessage("registration failed");
			printError("registration failed", ex);
		}
	}

	/**
	 * set online
	 * 
	 * @throws OperationFailedException
	 */
	public void setOnline(String userUri) throws OperationFailedException {

		Set<Entry<Object, ProtocolProviderFactory>> set = TcpInterfaceActivator
				.getProtocolProviderFactories().entrySet();
		for (Entry<Object, ProtocolProviderFactory> entry : set) {
			ProtocolProviderFactory providerFactory = (ProtocolProviderFactory) entry
					.getValue();
			ArrayList<AccountID> accountsList = providerFactory
					.getRegisteredAccounts();
			AccountID accountID;
			ServiceReference serRef;

			for (int i = 0; i < accountsList.size(); i++) {
				accountID = (AccountID) accountsList.get(i);
				boolean isHidden = (accountID.getAccountProperties().get(
						ProtocolProviderFactory.IS_PROTOCOL_HIDDEN) != null);
				if (isHidden)
					continue;

				ConfigurationService configService = TcpInterfaceActivator
						.getConfigurationService();
				serRef = providerFactory.getProviderForAccount(accountID);
				pps = (ProtocolProviderService) TcpInterfaceActivator.bundleContext
						.getService(serRef);

				/* add register listener */
				pps.addRegistrationStateChangeListener(this);

				String prefix = "net.java.sip.communicator.impl.gui.accounts";
				List<String> accounts = configService.getPropertyNamesByPrefix(
						prefix, true);
				Iterator<String> accountsIter = accounts.iterator();
				while (accountsIter.hasNext()) {
					String accountRootPropName = (String) accountsIter.next();
					printMessage(accountRootPropName);
					String accountUID = configService
							.getString(accountRootPropName);
					if ((accountUID.equals(pps.getAccountID()
							.getAccountUniqueID()))
							&& (userUri.equals(accountUID.substring(4,
									accountUID.length())))) {
						pps.register(GuiActivator.getUIService()
								.getDefaultSecurityAuthority(pps));
						break;
					}
				}
			}
		}
	}

	/**
	 * unregister the accounts
	 * 
	 */

	public void unregister() {

		try {

			ProtocolProviderFactory providerFactory = TcpInterfaceActivator
					.getProtocolProviderFactory(pps);
			ConfigurationService confService = TcpInterfaceActivator
					.getConfigurationService();
			String prefix = "net.java.sip.communicator.impl.gui.accounts";
			List<String> accounts = confService.getPropertyNamesByPrefix(
					prefix, true);
			for (String accountRootPropName : accounts) {
				String accountUID = confService.getString(accountRootPropName);

				if (accountUID.equals(pps.getAccountID().getAccountUniqueID())) {
					confService.setProperty(accountRootPropName, null);
					break;
				}
			}

			providerFactory.uninstallAccount(pps.getAccountID());

		} catch (Exception ex) {
			printError("de-registration failed", ex);
			// sendMessage("de-registration failed");
		} finally {

			if (inComingCallDialog != null)
				CallManager.disposeCallDialogWait(inComingCallDialog);

			if (callService != null) {
				callService.removeCallListener(this);
			}

			if (incomingcall != null) {
				incomingcall.removeCallChangeListener(this);
			}

			if (outgoingcall != null) {

				Iterator<CallParticipant> participants = outgoingcall
						.getCallParticipants();
				while (participants.hasNext()) {
					CallParticipant participant = (CallParticipant) participants
							.next();
					participant.removeCallParticipantListener(this);
				}
			}

			pps = null;
			outgoingcall = null;
			incomingcall = null;
			callService = null;
			inComingCallDialog = null;
		}
	}

	/**
	 * remove the account
	 * 
	 * @throws ParseException
	 * @throws OperationFailedException
	 * 
	 */

	public void removeAccount() {

		ConfigurationService configurationService = (ConfigurationService) TcpInterfaceActivator
				.getConfigurationService();

		// first retrieve all accounts that we've registered
		List<String> storedAccounts = configurationService
				.getPropertyNamesByPrefix(
						"net.java.sip.communicator.impl.protocol.sip", true);

		// find an account with the corresponding id.
		for (String accountRootPropertyName : storedAccounts) {
			List<String> accountPropertyNames = configurationService
					.getPropertyNamesByPrefix(accountRootPropertyName, false);

			// set all account properties to null in order to remove them.
			for (String propName : accountPropertyNames) {
				configurationService.setProperty(propName, null);
			}

			// and now remove the parent too.
			configurationService.setProperty(accountRootPropertyName, null);

		}
	}

	/**
	 * send INVITE
	 * 
	 * @param contact
	 *            whom to invite to be sent
	 * @throws OperationFailedException
	 * @throws ParseException
	 */
	public void invite(String contact) throws OperationFailedException,
			ParseException {

		OperationSetBasicTelephony telephony = (OperationSetBasicTelephony) pps
				.getOperationSet(OperationSetBasicTelephony.class);

		outgoingcall = telephony.createCall(contact);

		outgoingcall.getCallParticipants();
		Iterator<CallParticipant> participants = outgoingcall
				.getCallParticipants();
		while (participants.hasNext()) {
			CallParticipant participant = (CallParticipant) participants.next();
			participant.addCallParticipantListener(this);
		}
	}

	/**
	 * hang-up the call
	 * 
	 * @throws OperationFailedException
	 * 
	 */
	private void hangupCall() throws OperationFailedException {

		Iterator<CallParticipant> callParticipants = null;

		if (outgoingcall != null) {
			callParticipants = outgoingcall.getCallParticipants();
		} else if (incomingcall != null) {
			callParticipants = incomingcall.getCallParticipants();
		} else
			return;

		while (callParticipants.hasNext()) {
			CallParticipant participant = (CallParticipant) callParticipants
					.next();

			OperationSetBasicTelephony telephony = (OperationSetBasicTelephony) pps
					.getOperationSet(OperationSetBasicTelephony.class);
			telephony.hangupCallParticipant(participant);

		}

		if (inComingCallDialog != null)
			CallManager.disposeCallDialogWait(inComingCallDialog);

		incomingcall = null;
		inComingCallDialog = null;
		outgoingcall = null;
	}

	/**
	 * set OffLine
	 * 
	 * @throws OperationFailedException
	 * 
	 */

	public void setOffline() {
		try {

			pps.unregister();

			if (inComingCallDialog != null)
				CallManager.disposeCallDialogWait(inComingCallDialog);

		} catch (OperationFailedException ex) {

			printError("setOffline failed", ex);
			sendMessage("setOffline failed");

		} catch (Exception ex) {

			printError("setOffline failed", ex);
			sendMessage("setOffline failed");
		} finally {

			if (inComingCallDialog != null)
				CallManager.disposeCallDialogWait(inComingCallDialog);

			if (callService != null) {
				callService.removeCallListener(this);
			}

			if (incomingcall != null) {
				incomingcall.removeCallChangeListener(this);
			}

			if (outgoingcall != null) {

				Iterator<CallParticipant> participants = outgoingcall
						.getCallParticipants();
				while (participants.hasNext()) {
					CallParticipant participant = (CallParticipant) participants
							.next();
					participant.removeCallParticipantListener(this);
				}
			}

			inComingCallDialog = null;
			outgoingcall = null;
			incomingcall = null;
			callService = null;
			pps = null;
		}

	}

	/**
	 * accept the incoming call
	 * 
	 * @throws OperationFailedException
	 * 
	 */

	public void acceptIncomingCall() throws OperationFailedException {

		CallPanel callPanel = new CallPanel(incomingcall,
				GuiCallParticipantRecord.INCOMING_CALL);

		inComingCallDialog = CallManager.openCallDialog(callPanel);

		Iterator<CallParticipant> callParticipants = incomingcall
				.getCallParticipants();

		while (callParticipants.hasNext()) {
			CallParticipant participant = (CallParticipant) callParticipants
					.next();

			OperationSetBasicTelephony telephony = (OperationSetBasicTelephony) pps
					.getOperationSet(OperationSetBasicTelephony.class);
			telephony.answerCallParticipant(participant);
		}
	}

	/**
	 * reject the incoming call
	 * 
	 * @throws OperationFailedException
	 * 
	 */
	public void rejectIncomingCall() throws OperationFailedException {

		CallPanel callPanel = new CallPanel(incomingcall,
				GuiCallParticipantRecord.INCOMING_CALL);

		inComingCallDialog = CallManager.openCallDialog(callPanel);

		Iterator<CallParticipant> callParticipants = incomingcall
				.getCallParticipants();
		while (callParticipants.hasNext()) {
			CallParticipant participant = (CallParticipant) callParticipants
					.next();
			callService.hangupCallParticipant(participant);
		}

	}

	public void run() {

		try {

			// creating a socket to connect to the server
			clientSocket = new Socket(HOST_NAME, TCP_PORT);

			printMessage("Connected to localhost in port " + TCP_PORT);

			// get Input and Output streams
			dataOut = new DataOutputStream(clientSocket.getOutputStream());
			dataOut.flush();
			dataIn = new DataInputStream(clientSocket.getInputStream());

			// Communicating with the server
			while (!done) {
				String message = (String) dataIn.readUTF();
				printMessage("from miTester <===" + message);

				if (message.startsWith("REGISTER")) {
					String args[] = message.split(",");
					if (args.length == 2) {
						setOnline(args[1]);
					} else {

						printMessage(args[1] + " " + args[2] + " " + args[3]);
						register(args[1], args[2], args[3], args[4], args[5]);
					}
				} else if (message.startsWith(INVITE)) {
					String args[] = message.split(",");
					printMessage(args[1]);
					invite(args[1]);

				} else if (message.startsWith("ACCEPT")) {
					acceptIncomingCall();
				} else if (message.startsWith("REJECT")) {
					rejectIncomingCall();
				} else if (message.startsWith("CANCEL")
						&& (outgoingcall != null)) {
					if (outgoingcall.getCallState() != CallState.CALL_ENDED)
						hangupCall();
				} else if (message.startsWith("BYE")) {
					hangupCall();
				} else if (message.startsWith("DE-REGISTER-ALL")) {
					isDeRegisterAll = true;
					unregister();
				} else if (message.startsWith("DE-REGISTER")) {
					setOffline();
				} else if (message.startsWith("QUIT")) {

					GuiActivator.bundleContext.getBundle(0).stop();
				}
			}
		} catch (OperationFailedException ex) {
			printError("Operation Failed ", ex);
			sendMessage("Operation Failed");
		} catch (SocketException ex) {
			printError("Error in TCP socket ", ex);
			sendMessage("Operation Failed");
		} catch (IOException ex) {
			printError("Error in TCP Communication ", ex);
			sendMessage("Operation Failed");
		} catch (Exception ex) {
			printError("Error in communicating through TCP channel ", ex);
			sendMessage("Operation Failed");
		} finally {

			try {
				if (dataOut != null)
					dataOut.close();
				if (dataIn != null) {
					dataIn.close();
				}
				if (clientSocket != null) {
					clientSocket.close();
				}
				dataOut = null;
				dataIn = null;
				clientSocket = null;

				/* close all streams */
				close();

			} catch (Exception ex) {

			}
		}
	}

	/**
	 * Sending message to miTester
	 * 
	 * @param msg
	 *            a String message expected by miTester
	 */
	private void sendMessage(String msg) {
		try {
			if (dataOut != null) {
				dataOut.writeUTF(msg);
				dataOut.flush();
				printMessage(" to miTester ===>" + msg);
			}
		} catch (IOException ioExcp) {
			printMessage("Error in sending message to miTester" + msg);
		}
	}

	public void setExitThread(boolean done) {
		this.done = done;
	}

	/**
	 * This method is called to print the message in the console window
	 * 
	 * @param message
	 *            is a String message going to be printed on the console window
	 */
	public static void printMessage(String message) {
		try {
			SYSTEM_OUT.write(message);
			SYSTEM_OUT.write("\n");
			SYSTEM_OUT.flush();
		} catch (IOException ex) {

		}
	}

	/**
	 * This method is called to print the error message in the console window
	 * 
	 * @param message
	 *            is a String message going to be printed on the console window
	 */
	public static void printError(String errorMessage, Exception exception) {
		try {
			SYSTEM_ERR.write(errorMessage);
			SYSTEM_ERR.write("\n");
			SYSTEM_ERR.flush();
		} catch (IOException ex) {
		}
	}

	/**
	 * close all streams
	 * 
	 */
	public static void close() {
		try {
			SYSTEM_OUT.close();
			SYSTEM_ERR.close();
			SYSTEM_OUT.close();
		} catch (IOException ex) {

		}

	}

}