package net.riking.service;

import java.util.Map;

import net.riking.entity.model.SignIn;

public interface SignInService {

	public Map<String, Object> signIn(SignIn signIn, String userId, Integer integral);
}
