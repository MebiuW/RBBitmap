package mebiuw.rbb.fundation.bitstring;

import java.util.List;

import mebiuw.rbb.fundation.sql.Condition;

/**
 * 仅支持region id的表示范围在long以下的bitstring
 * @author MebiuW
 *
 */

public interface IBitString {
	public long getRegionId();
	public String getHexRegionIdString();
	public String toBinaryRegionIdString();
	public String toDecRegionIdString();
	public List<Long> getRegions(Condition con); 

}
