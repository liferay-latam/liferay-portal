package ${configYAML.apiPackagePath}.internal.resource.${escapedVersion}.factory;

import ${configYAML.apiPackagePath}.resource.${escapedVersion}.${schemaName}Resource;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.Company;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.model.Role;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.model.role.RoleConstants;
import com.liferay.portal.kernel.search.filter.Filter;
import com.liferay.portal.kernel.security.auth.PrincipalThreadLocal;
import com.liferay.portal.kernel.security.permission.PermissionChecker;
import com.liferay.portal.kernel.security.permission.PermissionCheckerFactory;
import com.liferay.portal.kernel.security.permission.PermissionThreadLocal;
import com.liferay.portal.kernel.security.permission.UserBag;
import com.liferay.portal.kernel.service.CompanyLocalService;
import com.liferay.portal.kernel.service.GroupLocalService;
import com.liferay.portal.kernel.service.ResourceActionLocalService;
import com.liferay.portal.kernel.service.ResourcePermissionLocalService;
import com.liferay.portal.kernel.service.RoleLocalService;
import com.liferay.portal.kernel.service.UserLocalService;
import com.liferay.portal.kernel.util.ListUtil;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.util.ProxyUtil;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.odata.filter.ExpressionConvert;
import com.liferay.portal.odata.filter.FilterParserProvider;
import com.liferay.portal.util.PropsValues;
import com.liferay.portal.vulcan.accept.language.AcceptLanguage;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.function.Function;

import javax.annotation.Generated;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.osgi.service.component.ComponentServiceObjects;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceScope;

/**
 * @author ${configYAML.author}
 * @generated
 */
@Component(<#if configYAML.liferayEnterpriseApp>enabled = false,</#if> immediate = true, service = ${schemaName}Resource.Factory.class)
@Generated("")
public class ${schemaName}ResourceFactoryImpl implements ${schemaName}Resource.Factory {

	@Override
	public ${schemaName}Resource.Builder create() {
		return new ${schemaName}Resource.Builder() {

			@Override
			public ${schemaName}Resource build() {
				if (_user == null) {
					throw new IllegalArgumentException("User is not set");
				}

				return _${schemaVarName}ResourceProxyProviderFunction.apply((proxy, method,arguments) -> _invoke(method, arguments, _checkPermissions, _httpServletRequest, _httpServletResponse, _preferredLocale, _user));
			}

			@Override
			public ${schemaName}Resource.Builder checkPermissions(boolean checkPermissions) {
				_checkPermissions = checkPermissions;

				return this;
			}

			@Override
			public ${schemaName}Resource.Builder httpServletRequest(HttpServletRequest httpServletRequest) {
				_httpServletRequest = httpServletRequest;

				return this;
			}

			@Override
			public ${schemaName}Resource.Builder httpServletResponse(HttpServletResponse httpServletResponse) {
				_httpServletResponse = httpServletResponse;

				return this;
			}

			@Override
			public ${schemaName}Resource.Builder preferredLocale(Locale preferredLocale) {
				_preferredLocale = preferredLocale;

				return this;
			}

			@Override
			public ${schemaName}Resource.Builder user(User user) {
				_user = user;

				return this;
			}

			private boolean _checkPermissions = true;
			private HttpServletRequest _httpServletRequest;
			private HttpServletResponse _httpServletResponse;
			private Locale _preferredLocale;
			private User _user;

		};
	}

	@Activate
	protected void activate() {
		${schemaName}Resource.FactoryHolder.factory = this;
	}

	@Deactivate
	protected void deactivate() {
		${schemaName}Resource.FactoryHolder.factory = null;
	}

	private static Function<InvocationHandler, ${schemaName}Resource> _getProxyProviderFunction() {
		Class<?> proxyClass = ProxyUtil.getProxyClass(${schemaName}Resource.class.getClassLoader(), ${schemaName}Resource.class);

		try {
			Constructor<${schemaName}Resource> constructor = (Constructor<${schemaName}Resource>)proxyClass.getConstructor(InvocationHandler.class);

			return invocationHandler -> {
				try {
					return constructor.newInstance(invocationHandler);
				}
				catch (ReflectiveOperationException reflectiveOperationException) {
					throw new InternalError(reflectiveOperationException);
				}
			};
		}
		catch (NoSuchMethodException noSuchMethodException) {
			throw new InternalError(noSuchMethodException);
		}
	}

	private Object _invoke(Method method, Object[] arguments, boolean checkPermissions, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Locale preferredLocale, User user) throws Throwable {
		String name = PrincipalThreadLocal.getName();

		PrincipalThreadLocal.setName(user.getUserId());

		PermissionChecker permissionChecker = PermissionThreadLocal.getPermissionChecker();

		if (checkPermissions) {
			PermissionThreadLocal.setPermissionChecker(_defaultPermissionCheckerFactory.create(user));
		}
		else {
			PermissionThreadLocal.setPermissionChecker(new LiberalPermissionChecker(user));
		}

		${schemaName}Resource ${schemaVarName}Resource = _componentServiceObjects.getService();

		${schemaVarName}Resource.setContextAcceptLanguage(new AcceptLanguageImpl(httpServletRequest, preferredLocale, user));

		Company company = _companyLocalService.getCompany(user.getCompanyId());

		${schemaVarName}Resource.setContextCompany(company);

		${schemaVarName}Resource.setContextHttpServletRequest(httpServletRequest);
		${schemaVarName}Resource.setContextHttpServletResponse(httpServletResponse);
		${schemaVarName}Resource.setContextUser(user);
		${schemaVarName}Resource.setExpressionConvert(_expressionConvert);
		${schemaVarName}Resource.setFilterParserProvider(_filterParserProvider);
		${schemaVarName}Resource.setGroupLocalService(_groupLocalService);
		${schemaVarName}Resource.setResourceActionLocalService(_resourceActionLocalService);
		${schemaVarName}Resource.setResourcePermissionLocalService(_resourcePermissionLocalService);
		${schemaVarName}Resource.setRoleLocalService(_roleLocalService);

		try {
			return method.invoke(${schemaVarName}Resource, arguments);
		}
		catch (InvocationTargetException invocationTargetException) {
			throw invocationTargetException.getTargetException();
		}
		finally {
			_componentServiceObjects.ungetService(${schemaVarName}Resource);

			PrincipalThreadLocal.setName(name);

			PermissionThreadLocal.setPermissionChecker(permissionChecker);
		}
	}

	private static final Function<InvocationHandler, ${schemaName}Resource> _${schemaVarName}ResourceProxyProviderFunction = _getProxyProviderFunction();

	private static final Log _log = LogFactoryUtil.getLog(
		${schemaName}ResourceFactoryImpl.class);

	@Reference
	private CompanyLocalService _companyLocalService;

	@Reference(scope = ReferenceScope.PROTOTYPE_REQUIRED)
	private ComponentServiceObjects<${schemaName}Resource> _componentServiceObjects;

	@Reference
	private PermissionCheckerFactory _defaultPermissionCheckerFactory;

	@Reference(
		target = "(result.class.name=com.liferay.portal.kernel.search.filter.Filter)"
	)
	private ExpressionConvert<Filter> _expressionConvert;

	@Reference
	private FilterParserProvider _filterParserProvider;

	@Reference
	private GroupLocalService _groupLocalService;

	@Reference
	private ResourceActionLocalService _resourceActionLocalService;

	@Reference
	private ResourcePermissionLocalService _resourcePermissionLocalService;

	@Reference
	private RoleLocalService _roleLocalService;

	@Reference
	private UserLocalService _userLocalService;

	private class AcceptLanguageImpl implements AcceptLanguage {

		public AcceptLanguageImpl(HttpServletRequest httpServletRequest, Locale preferredLocale, User user) {
			_httpServletRequest = httpServletRequest;
			_preferredLocale = preferredLocale;
			_user = user;
		}

		@Override
		public List<Locale> getLocales() {
			return Arrays.asList(getPreferredLocale());
		}

		@Override
		public String getPreferredLanguageId() {
			return LocaleUtil.toLanguageId(getPreferredLocale());
		}

		@Override
		public Locale getPreferredLocale() {
			if (_preferredLocale != null) {
				return _preferredLocale;
			}

			if (_httpServletRequest != null) {
				Locale locale = (Locale)_httpServletRequest.getAttribute(WebKeys.LOCALE);

				if (locale != null) {
					return locale;
				}
			}

			return _user.getLocale();
		}

		@Override
		public boolean isAcceptAllLanguages() {
			return false;
		}

		private final HttpServletRequest _httpServletRequest;
		private final Locale _preferredLocale;
		private final User _user;

	}

	private class LiberalPermissionChecker implements PermissionChecker {

		@Override
		public PermissionChecker clone() {
			return this;
		}

		@Override
		public long getCompanyId() {
			return _user.getCompanyId();
		}

		@Override
		public long[] getGuestUserRoleIds() {
			return PermissionChecker.DEFAULT_ROLE_IDS;
		}

		@Override
		public long getOwnerRoleId() {
			return _ownerRole.getRoleId();
		}

		@Override
		public Map<Object, Object> getPermissionChecksMap() {
			return new HashMap<>();
		}

		@Override
		public long[] getRoleIds(long userId, long groupId) {
			return PermissionChecker.DEFAULT_ROLE_IDS;
		}

		@Override
		public User getUser() {
			return _user;
		}

		@Override
		public UserBag getUserBag() {
			return null;
		}

		@Override
		public long getUserId() {
			return _user.getUserId();
		}

		@Override
		public boolean hasOwnerPermission(
			long companyId, String name, long primKey, long ownerId,
			String actionId) {

			return true;
		}

		@Override
		public boolean hasOwnerPermission(
			long companyId, String name, String primKey, long ownerId,
			String actionId) {

			return true;
		}

		@Override
		public boolean hasPermission(
			Group group, String name, long primKey, String actionId) {

			return true;
		}

		@Override
		public boolean hasPermission(
			Group group, String name, String primKey, String actionId) {

			return true;
		}

		@Override
		public boolean hasPermission(
			long groupId, String name, long primKey, String actionId) {

			return true;
		}

		@Override
		public boolean hasPermission(
			long groupId, String name, String primKey, String actionId) {

			return true;
		}

		@Override
		public void init(User user) {
			_user = user;

			try {
				_ownerRole = _roleLocalService.getRole(
					user.getCompanyId(), RoleConstants.OWNER);
			}
			catch (Exception exception) {
				_log.error(exception);
			}
		}

		@Override
		public boolean isCheckGuest() {
			return PropsValues.PERMISSIONS_CHECK_GUEST_ENABLED;
		}

		@Override
		public boolean isCompanyAdmin() {
			return true;
		}

		@Override
		public boolean isCompanyAdmin(long companyId) {
			return true;
		}

		@Override
		public boolean isContentReviewer(long companyId, long groupId) {
			return true;
		}

		@Override
		public boolean isGroupAdmin(long groupId) {
			return true;
		}

		@Override
		public boolean isGroupMember(long groupId) {
			return true;
		}

		@Override
		public boolean isGroupOwner(long groupId) {
			return true;
		}

		@Override
		public boolean isOmniadmin() {
			return true;
		}

		@Override
		public boolean isOrganizationAdmin(long organizationId) {
			return true;
		}

		@Override
		public boolean isOrganizationOwner(long organizationId) {
			return true;
		}

		@Override
		public boolean isSignedIn() {
			return true;
		}

		private LiberalPermissionChecker(User user) {
			init(user);
		}

		private Role _ownerRole;
		private User _user;

	}

}