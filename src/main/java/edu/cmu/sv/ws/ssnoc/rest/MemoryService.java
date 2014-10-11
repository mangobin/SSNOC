package edu.cmu.sv.ws.ssnoc.rest;

import java.io.File;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import javax.ws.rs.DELETE;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

import edu.cmu.sv.ws.ssnoc.common.logging.Log;
import edu.cmu.sv.ws.ssnoc.common.utils.TimestampUtil;
import edu.cmu.sv.ws.ssnoc.data.dao.DAOFactory;
import edu.cmu.sv.ws.ssnoc.data.po.MemoryPO;

@Path("/memory")
public class MemoryService extends BaseService {

	private static Timer timer;
	
	@POST
	@Path("/start")
	public void startMemoryMeasurement() {
		
		timer = new Timer();
		
		timer.schedule(new TimerTask() {
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
			}, 1*60*1000);
		
		
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
			}, 1*60*1000, 1*60*1000);
	
	}
	
	@POST
	@Path("/stop")
	public void stopMemoryMeasurement() {
		Log.enter("stop the memory measurement");
		timer.cancel();
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
