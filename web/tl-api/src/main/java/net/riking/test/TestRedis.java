package net.riking.test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.riking.core.entity.model.ModelPropDict;
import net.riking.util.RedisUtil;

public class TestRedis {
	
	public static void main(String[] args) {
//		RedisUtils redisCli = new RedisUtils();
//		// redisCli.KeyOperate();
//		redisCli.SetOperate();
		
//		 Map<String, Map<String, List<ModelPropDict>>> map = new HashMap<String, Map<String, List<ModelPropDict>>>();
//	       Map<String, List<ModelPropDict>> map2 = new HashMap<String, List<ModelPropDict>>();
//	       
//	       List<ModelPropDict> list = new ArrayList<ModelPropDict>();
//	       ModelPropDict m = new ModelPropDict();
//	       m.setId((long)1);
//	       m.setClazz("T_REPORT_LIST");
//	       m.setField("REPORTUNIT");
//	       m.setKe("W");
//	       m.setValu("万元");
//	       list.add(m);
//	       
//	       m = new ModelPropDict();
//	       m.setId((long)2);
//	       m.setClazz("T_REPORT_LIST");
//	       m.setField("REPORTUNIT");
//	       m.setKe("Y");
//	       m.setValu("元");
//	       list.add(m);
//	       
//	       m = new ModelPropDict();
//	       m.setId((long)3);
//	       m.setClazz("T_REPORT_LIST");
//	       m.setField("REPORTUNIT");
//	       m.setKe("K");
//	       m.setValu("千元");
//	       list.add(m);
//	       map2.put("REPORTUNIT", list);
//	       map.put("T_REPORT_LIST", map2);
//	       
//	       
//	       list = new ArrayList<ModelPropDict>();
//	       map2 = new HashMap<String, List<ModelPropDict>>();
//	       m = new ModelPropDict();
//	       m.setId((long)4);
//	       m.setClazz("T_MODEL_AML_CORPTRN");
//	       m.setField("BZ");
//	       m.setKe("JPY");
//	       m.setValu("JPY-日元");
//	       list.add(m);
//	       
//	       list = new ArrayList<ModelPropDict>();
//	       m = new ModelPropDict();
//	       m.setId((long)5);
//	       m.setClazz("T_MODEL_AML_CORPTRN");
//	       m.setField("BZ");
//	       m.setKe("KES");
//	       m.setValu("KES-肯尼亚先令");
//	       list.add(m);
//	       map2.put("BZ", list);
//	       map.put("T_MODEL_AML_CORPTRN", map2);
//	       
//	       
	       RedisUtil redis = RedisUtil.getInstall();
//			for(String key : map.keySet()){
//				Map<String, List<ModelPropDict>> modelPropDictMap = map.get(key);
//				
////				for(String key2 : modelPropDictMap.keySet()){
////					List<ModelPropDict> modelPropDictList = modelPropDictMap.get(key2);
////					
//////					byte[] b = SerializeUtil.serialize(modelPropDictList);
////					
////					System.out.println("key="+key);
////					System.out.println("key2="+key2);
////					long  l = jedis.HASH.hset(key, key2, "123");
////					System.out.println("KEY="+key+", key2="+ key2 + ", 保存状态："+(l==1?"成功":"失败"));
////				}
//				redis.setMap(key, modelPropDictMap);
//				
//			}
			
			Map<String, List<ModelPropDict>> returnMap = redis.getMap("T_REPORT_LIST");
			for(String key : returnMap.keySet()){
				List<ModelPropDict> returnList = returnMap.get(key);
				for(ModelPropDict data : returnList){
					System.out.println(data.getClazz());
					System.out.println(data.getField());
					System.out.println(data.getKe());
					System.out.println(data.getValu());
				}
			}
	}

}
