import java.util.*;

public class ServerFile {
	// the id of the server to which the original copy of the file belongs to
	public int homeServer;
	// the name of the file
	public String name;
	// the identifier of the file
	public String identifier;
	// Name of the owner of the file
	public String owner;
	// When the file was modified
	public String data;
	// size of the file
	public int size;
	// number of chunks
	public int chunksNo;
	// to save the chunks
	public ArrayList<Chunk> chunks = new ArrayList<Chunk>();

	// constructor
	public ServerFile(int homeServer, String name, int size, String owner) {

		this.homeServer = homeServer;
		this.name = name;
		this.size = size;
		this.owner = owner;
		this.chunksNo = this.size / 64000 + 1;
		
		System.out.println("File belongs to server: " + this.homeServer);
		System.out.println("Filename: " + this.name);
		System.out.println("File size: " + this.size);
		System.out.println("Owner of the file: " + this.owner);
		System.out.println("Number of chunks to be used: " + this.chunksNo);
		
		int missedSize = this.size;
		int filledSize = 0;
		
		for(int i = 0; i < this.chunksNo; i++){
			if(missedSize >= 64000){
				
				filledSize = 64000;
				missedSize = missedSize - filledSize;
				
				System.out.println("Chunk Size --> " + filledSize);
				Chunk chunk = new Chunk(1, i, filledSize);
				chunks.add(chunk);
				continue;
				
			}
			else if(missedSize < 64000){
				
				filledSize = missedSize;
				
				System.out.println("Chunk Size --> " + filledSize);
				Chunk chunk = new Chunk(1, i, filledSize);
				chunks.add(chunk);
				continue;
				
			}
		}

	}

}
