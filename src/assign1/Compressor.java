package assign1;
/**
 * Assignment 1 Submitted by: Javier Giberg. ID# 302280383 Netanel Bitton. ID#
 * 305484651
 */
public interface Compressor
{
	abstract public void Compress(String[] input_names, String[] output_names);
	abstract public void Decompress(String[] input_names, String[] output_names);

	//abstract public byte[] CompressWithArray(String[] input_names, String[] output_names);
	//abstract public byte[] DecompressWithArray(String[] input_names, String[] output_names);
}
