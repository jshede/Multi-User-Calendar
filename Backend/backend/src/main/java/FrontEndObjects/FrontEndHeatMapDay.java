package FrontEndObjects;

import java.io.Serializable;

import com.server.backend.HeatMapDay;

public class FrontEndHeatMapDay implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public int id;
	public int groupId;
	public String date;
	public double[] usersBusy;

	public FrontEndHeatMapDay(HeatMapDay hmd) {
		this.id = hmd.getId().intValue();
		this.groupId = hmd.getGroup().getId().intValue();
		this.date = hmd.getDate().toString();
		int size = hmd.getGroup().getSize().intValue();
		int[] usersBusy = hmd.getUsersBusy();
		for (int i = 0; i < usersBusy.length; i++) {
			this.usersBusy[i] = 1.0 * usersBusy[i] / size;
		}
	}
}