package assign1;
/**
 * Assignment 1 Submitted by: Javier Giberg. ID# 302280383 Netanel Bitton. ID#
 * 305484651
 */
public class Runner {

	public static void main(String[] args) {
		HuffmanEncoderDecoder objectC = new HuffmanEncoderDecoder();
		HuffmanEncoderDecoder objectD = new HuffmanEncoderDecoder();
		//Romeo and Juliet  Entire Play.txt
		//Smiley.bmp
		String src = "Romeo and Juliet  Entire Play.txt";
//-----------------------Compress------------------------------------		
		String[] input_names = {src};
		String[] output_names = {"Compressed file.bin"};
		objectC.Compress(input_names, output_names);
	    System.out.println("File: "+src+" Compresses!");
//-----------------------Decompress----------------------------------    
        String[] srcFile = {"Compressed file.bin"};
        String[] dstFile = {"new "+src};
        objectD.Decompress(srcFile,dstFile);
        System.out.println("File: "+src+" Descompress!");

	}

}
