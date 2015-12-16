package com.ncu.testbank.base.filter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authz.AuthorizationFilter;

/**
 * 改变shiro框架的角色判断and关系
 * @author Jedeft
 *
 */
public class CustomRolesAuthorizationFilter extends AuthorizationFilter {
	@Override
	protected boolean isAccessAllowed(ServletRequest request,
			ServletResponse response, Object mappedValue) throws Exception {
		Subject subject = getSubject(request, response);
		String[] rolesArray = (String[]) mappedValue;

		if (rolesArray == null || rolesArray.length == 0) {
			return true;
		}
		for (String roleName : rolesArray) {
			if (subject.hasRole(roleName)) {
				return true;
			}
		}
		return false;
	}
}