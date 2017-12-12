package net.riking.util;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import net.riking.core.entity.model.ModelPropDict;
import net.riking.service.SysDataService;

public class GradeCalUtils {
	@Autowired
	private static SysDataService sysDataService;

	public static Integer transformExpToGrade() {
		List<ModelPropDict> propDicts = sysDataService.getDicts("T_APP_USER", "GRADE_RANGE");
		Integer begin = 0;
		Integer gradeOneMax = 0;
		Integer gradeTwoMax = 0;
		Integer gradeThrMax = 0;
		Integer gradeFourMax = 0;
		for (ModelPropDict modelPropDict : propDicts) {
			if ("V1".equals(modelPropDict)) {
				gradeOneMax = Integer.parseInt(modelPropDict.getValu());
			}
		}
		return null;
	}
}
