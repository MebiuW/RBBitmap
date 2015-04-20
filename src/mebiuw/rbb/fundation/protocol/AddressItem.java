package mebiuw.rbb.fundation.protocol;

import java.util.ArrayList;
import java.util.List;
/***
 * 用来保存地址的
 * @author 72770_000
 *
 */

public class AddressItem {
	String ip;
	List<Integer> ports;
	Long chordid;

	/**
	 * 采用配置文件的方式
	 * @param ip
	 * @param ports
	 */
	public AddressItem(String ip, String ports,long chordid){
		this.ip=ip;
		this.ports = new ArrayList<Integer>();
		String[] portarray = ports.split(" ");
		for(int i=0;i<portarray.length;i++){
			this.ports.add(Integer.parseInt(portarray[i]));
		}
		this.chordid=(long) chordid;
	}

	/**
	 * 只有一个端口的情况下
	 * 
	 * @param chordid
	 * @param ip
	 * @param port
	 */
	public AddressItem(Long chordid, String ip, int port) {
		this.ip = ip;
		this.ports = new ArrayList<Integer>();
		this.ports.add(port);
		this.chordid = chordid;
	}

	/**
	 * 有多个端口的情况
	 * 
	 * @param chordid
	 * @param ip
	 * @param ports
	 */
	public AddressItem(Long chordid, String ip, List<Integer> ports) {
		this.ip = ip;
		this.ports = new ArrayList<Integer>();
		this.ports = ports;
		this.chordid = chordid;
	}

	/**
	 * 增加端口
	 * 
	 * @param port
	 * @return
	 */
	public boolean addPort(int port) {
		try {
			this.ports.add(port);
		} catch (Exception e) {
			return false;
		}
		return true;
	}
	/**
	 * 删除端口
	 * @param port
	 */
	public void removePort(int port){
		this.ports.remove(port);
	}

	public String getIp() {
		return ip;
	}

	public List<Integer> getPorts() {
		return ports;
	}

	public Long getChordid() {
		return chordid;
	}


}