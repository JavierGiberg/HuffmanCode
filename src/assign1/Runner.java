package assign1;

public class Runner {

	public static void main(String[] args) {
		HuffmanEncoderDecoder a = new HuffmanEncoderDecoder();
		
//		//Romeo and Juliet  Entire Play.txt
//		//Smiley.bmp
		String[] input_names = {"Smiley.bmp"};
		String[] output_names = {"test.bin"};
		a.Compress(input_names, output_names);
	    System.out.println("File Compresses!");
     
        String[] srcFile = {"test.bin"};
        String[] dstFile = {"finish.bmp"};
        a.Decompress(srcFile,dstFile);
        System.out.println("File Descompress!");

	}

}
