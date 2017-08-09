package net.riking.test;

import java.util.Map;
import java.util.Set;

import net.riking.service.impl.GetDateServiceImpl;

public class TestJG {

	public static void main(String[] args) {
		//Jdpush.testSendPush("f7ac0692d540d2a7e15613bb", "ae4b5cb2379495f2303019ff");
		GetDateServiceImpl   Test = new GetDateServiceImpl();
		Map<String, Set<String>> map = Test.getMounthWeek("201708");
	}

}
