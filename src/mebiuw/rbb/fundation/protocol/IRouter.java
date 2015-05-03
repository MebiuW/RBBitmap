package mebiuw.rbb.fundation.protocol;
/**
 * 实现一个Router应该做到的
 * @author 72770_000
 *
 */
public interface IRouter {
	public void addAddressItem(AddressItem item);
	public int toNextHop(long chordid);
	public void removeAddressItem(String ip);
	

}
