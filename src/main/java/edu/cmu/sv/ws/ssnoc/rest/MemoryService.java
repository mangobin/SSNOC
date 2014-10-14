package edu.cmu.sv.ws.ssnoc.rest;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import edu.cmu.sv.ws.ssnoc.common.logging.Log;
import edu.cmu.sv.ws.ssnoc.common.utils.ConverterUtils;
import edu.cmu.sv.ws.ssnoc.common.utils.TimestampUtil;
import edu.cmu.sv.ws.ssnoc.data.dao.DAOFactory;
import edu.cmu.sv.ws.ssnoc.data.po.MemoryPO;
import edu.cmu.sv.ws.ssnoc.dto.Memory;

@Path("/memory")
public class MemoryService extends BaseService {

	private static Timer timer;
	private static int SAMPLING_INTERVAL = 60 * 1000; // 1 minute sampling interval
	
	@POST
	@Path("/start")
	public void startMemoryMeasurement() {
		if(timer == null) {
			timer = new Timer();
			timer.scheduleAtFixedRate(new TimerTask() {
				  @Override
				  public void run() {
					  Runtime runtime = Runtime.getRuntime();
						int usedMemory = (int) (runtime.totalMemory() /1024);
						int freeMemory = (int) (runtime.freeMemory() /1024);
					    File[] roots = File.listRoots();
					    int totalPersistent = (int) roots[0].getTotalSpace()/1024;
					    int freePersistent = (int) roots[0].getUsableSpace()/1024;
					    int usedPersistent = totalPersistent - freePersistent;
					    
					    Date date = new Date();
					    String temp = TimestampUtil.convert(date);
					    Date date2 = TimestampUtil.convert(temp);
					    
					    MemoryPO po = new MemoryPO(date2, usedMemory, freeMemory, usedPersistent, freePersistent);
					    DAOFactory.getInstance().getMemoryDAO().save(po);
				  }
				}, SAMPLING_INTERVAL, SAMPLING_INTERVAL);
		}
	}
	
	@POST
	@Path("/stop")
	public void stopMemoryMeasurement() {
		Log.enter("stop the memory measurement");
		if(timer != null){
			timer.cancel();
			timer = null;
		}	
	}
	
	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	public List<Memory> get24HourMemorySnapshot(){
		return getSnapshotFromHoursAgo(24);
	}
	
	@GET
	@Path("/interval/{timeWindowInHours}")
	@Produces({ MediaType.APPLICATION_JSON })
	public List<Memory> getSnapshotFromHoursAgo(@PathParam("timeWindowInHours") int hoursAgo){
		Log.enter("getting memory from last " + hoursAgo + " hours");
		
		Calendar calendar = Calendar.getInstance();
		//calendar.add(Calendar.HOUR_OF_DAY, -hoursAgo);
		calendar.add(Calendar.MINUTE, -hoursAgo); // for testing purposes
		Date date = calendar.getTime();

	    List<MemoryPO> memoryPOs = DAOFactory.getInstance().getMemoryDAO().findMemorySinceDate(date);
	    List<Memory> list = new ArrayList<Memory>();
	    for(MemoryPO po : memoryPOs){
	    	Memory dto = ConverterUtils.convert(po);
	    	list.add(dto);
	    }
	    
	    Log.exit(list);
	    return list;
	}
	
	@DELETE
	public void deleteAllMemoryCrumbs() {
		Log.enter("delete all the memory crumbs");
		DAOFactory.getInstance().getMemoryDAO().deleteAllMemoryCrumbs();
		
	}
	
	public static void sleep(int minute) {
		try {
			Thread.sleep(minute*60*1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
