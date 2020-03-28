package com.server.backend.Controllers;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.server.backend.HeatMapDay;
import com.server.backend.HeatMapDayRepository;
import com.server.backend.services.RoleService;
import com.server.backend.staticHelpers.userHelper;

import FrontEndObjects.FrontEndHeatMapDay;

@Controller
public class HeatMapController {

	@Autowired
	HeatMapDayRepository hmRepo;
	
	@Autowired
	RoleService roles;
	
	/**
	 * Gets a heat map of availability of members in the specified group from the start date to end date.
	 * @param session No need to provide
	 * @param groupId Group to get heatMap of.
	 * @param startDate The first day to get heat map data for.
	 * @param endDate The last day to get heat map data for.
	 * @return the requested heat map.
	 */
	@GetMapping("/api/getHeatMap")
	public List<FrontEndHeatMapDay> getHeatMap(HttpSession session, @RequestParam int groupId, @RequestParam String startDate, @RequestParam String endDate){
		if(roles.isAtLeastModerator(userHelper.sessionToUserId(session), groupId))
			return null;
		ArrayList<FrontEndHeatMapDay> heatMap = new ArrayList<>();
		for(HeatMapDay hmd: hmRepo.findAll()) {
			heatMap.add(new FrontEndHeatMapDay(hmd));
		}
		heatMap.sort(new Comparator<FrontEndHeatMapDay>() {
			@Override
			public int compare(FrontEndHeatMapDay arg0, FrontEndHeatMapDay arg1) {
				return (Date.valueOf(arg0.date)).compareTo(Date.valueOf(arg1.date));
			}
		});
		return heatMap;
	}
}
