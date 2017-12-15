package net.riking.service;

import net.riking.entity.model.SignIn;

public interface SignInService {

	public Integer signIn(SignIn signIn, String userId, Integer integral);
}
