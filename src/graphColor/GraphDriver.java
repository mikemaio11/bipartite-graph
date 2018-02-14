package graphColor;
import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class GraphDriver extends GraphMaker{
	public static void main(String [] args) {
		Scanner sc = new Scanner(System.in);
		System.out.print("Enter filename: ");
		String file = sc.nextLine();
		String output = file;
		output = file.split("\\.")[0];
		buildGraph(file, new File("../\\output_files\\"+output+"_output.txt"));
		try {
			Desktop.getDesktop().open(new File("../\\output_files\\"+output+"_output.txt"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		sc.close();
	}
}