import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.DatagramPacket;
import java.util.ArrayList;

public class CounterBackup implements Runnable {

	public Server server;
	public ArrayList<Message> sendQueue = new ArrayList<Message>(); 

	public CounterBackup(Server server) {

		this.server = server;

	}

	public void createCounterControl() {

		Thread counterBackup = new Thread(this);
		counterBackup.start();

	}

	@Override
	public void run() {

		byte[] buffer;
		DatagramPacket toReceive;

		while (true) {

			try {

				buffer = new byte[256];
				toReceive = new DatagramPacket(buffer, buffer.length, server.multicast.backupIP,
						server.multicast.backupPort);

				server.multicast.backupSocket.receive(toReceive);

				Message message = parseMessage(toReceive.getData());
				
				if(message != null){
					server.messages.add(message);
				}

			} catch (IOException e) {
				e.printStackTrace();
			}

		}
	}

	public Message parseMessage(byte[] message) throws UnsupportedEncodingException {

		Message m;

		ByteArrayInputStream data = new ByteArrayInputStream(message);
		byte[] chunkData = null;
		byte[] header = null;

		for (int i = 0; i < message.length - 1; i++) {

			if (message[i] == 0xd) {

				if (message[i + 1] == 0xa) {

					header = new byte[i + 3];
					chunkData = new byte[message.length - (i + 1)];

					data.read(header, 0, i + 3);
					data.read(chunkData, 0, message.length - (i + 1));
					break;
				}
			}

		}

		// split the information on the received request
		String headerString = new String(header, "UTF-8");
		String[] messageSplit;

		messageSplit = headerString.split(" +");

		if (messageSplit[0].equals("PUTCHUNK")) {

			m = new Message(messageSplit[0], messageSplit[1], messageSplit[2], messageSplit[3],
					Integer.parseInt(messageSplit[4]), Integer.parseInt(messageSplit[5]), chunkData);
			
		} else {
			System.out.println("Received message that is not supported by the system.... Bye....");
			m = null;
		}
		
		return m;

	}

}
