import java.util.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class FileEvent {
	// to define the chunk's size
	public int CHUNK_SIZE = 64000;
	// the id of the server to which the original copy of the file belongs to
	public String homeServer;
	// the name of the file
	public String name;
	// the identifier of the file
	public String identifier;
	// When the file was modified
	public String date;
	// size of the file
	public int size;
	// number of chunks
	public int chunksNo;
	// to save the requested replication Degree
	public int replicationDegree;
	// to save the chunks
	public ArrayList<Chunk> chunks = new ArrayList<Chunk>();

	// constructor
	public FileEvent(String homeServer, String name, int size, String date) {

		this.homeServer = homeServer;
		this.name = name;
		this.size = size;
		this.date = date;
		this.chunksNo = (this.size / (64000)) + 1;

		try {
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			String id = name + date;
			md.update(id.getBytes());
			byte[] digest = md.digest();
			this.identifier = String.format("%064x", new java.math.BigInteger(1, digest));
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}

		System.out.println("Number of chunks to be used: " + this.chunksNo);

	}

	// splits the file in chunks
	public void splitFile(File inputFile, Server server) {

		FileInputStream inputStream;
		String newFileName;
		FileOutputStream filePart;

		System.out.println("...splitting file...");

		int fileSize = (int) inputFile.length();
		int nChunks = 0, read = 0, readLength = CHUNK_SIZE;
		byte[] byteChunkPart;

		try {
			inputStream = new FileInputStream(inputFile);
			while (fileSize > 0) {
				if (fileSize <= 64000) {
					readLength = fileSize;
				}

				byteChunkPart = new byte[readLength];
				read = inputStream.read(byteChunkPart, 0, readLength);
				fileSize -= read;

				assert (read == byteChunkPart.length);
				nChunks++;
				newFileName = identifier + "_" + Integer.toString(nChunks - 1);

				filePart = new FileOutputStream(new File(newFileName));
				filePart.write(byteChunkPart);

				Chunk chunk = new Chunk(identifier, nChunks - 1, byteChunkPart, replicationDegree, 0);
				chunks.add(chunk);

				filePart.flush();
				filePart.close();
				byteChunkPart = null;
				filePart = null;
			}
			inputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("splitted file");

	}

	// hash the string (to create file identifier)
	public byte[] hash(String text) throws NoSuchAlgorithmException {

		MessageDigest digest = MessageDigest.getInstance("SHA-256");
		byte[] hash = digest.digest(text.getBytes(StandardCharsets.UTF_8));

		return hash;
	}

}
