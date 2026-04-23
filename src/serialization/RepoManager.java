package serialization;

import java.io.*;
import implementations.BSTree;
import implementations.Word;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class RepoManager {
	
	private static final String FILE_NAME = "repository.ser";
	
	public static void save(BSTree<Word> tree) {
		try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
			
			oos.writeObject(tree);
		}
		
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings("unchecked")
	public static BSTree<Word> load() {
		File f = new File(FILE_NAME);
		if (!f.exists()) {
			return new BSTree<>();
		}
		
		try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_NAME))) {
			
			return (BSTree<Word>) ois.readObject();
		}
		
		catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
			return new BSTree<>();
		}
	}
}
