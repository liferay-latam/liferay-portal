/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package com.liferay.commerce.shipping.web.internal.frontend.taglib.servlet.taglib;

import com.liferay.commerce.product.model.CommerceChannel;
import com.liferay.commerce.product.service.CommerceChannelLocalService;
import com.liferay.commerce.service.CommerceShippingMethodService;
import com.liferay.commerce.shipping.engine.fixed.service.CommerceShippingFixedOptionService;
import com.liferay.commerce.shipping.web.internal.display.context.CommerceShippingMethodsDisplayContext;
import com.liferay.commerce.util.CommerceShippingEngineRegistry;
import com.liferay.frontend.taglib.servlet.taglib.ScreenNavigationCategory;
import com.liferay.frontend.taglib.servlet.taglib.ScreenNavigationEntry;
import com.liferay.frontend.taglib.servlet.taglib.util.JSPRenderer;
import com.liferay.portal.kernel.language.Language;
import com.liferay.portal.kernel.service.CountryService;
import com.liferay.portal.kernel.util.ResourceBundleUtil;
import com.liferay.portal.kernel.util.WebKeys;

import java.io.IOException;

import java.util.Locale;
import java.util.ResourceBundle;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Alessio Antonio Rendina
 */
@Component(
	property = {
		"screen.navigation.category.order:Integer=70",
		"screen.navigation.entry.order:Integer=10"
	},
	service = {ScreenNavigationCategory.class, ScreenNavigationEntry.class}
)
public class CommerceChannelShippingRestrictionsScreenNavigationCategory
	implements ScreenNavigationCategory,
			   ScreenNavigationEntry<CommerceChannel> {

	@Override
	public String getCategoryKey() {
		return "shipping-restrictions";
	}

	@Override
	public String getEntryKey() {
		return getCategoryKey();
	}

	@Override
	public String getLabel(Locale locale) {
		ResourceBundle resourceBundle = ResourceBundleUtil.getBundle(
			"content.Language", locale, getClass());

		return _language.get(resourceBundle, getCategoryKey());
	}

	@Override
	public String getScreenNavigationKey() {
		return "commerce.channel.general";
	}

	@Override
	public void render(
			HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse)
		throws IOException {

		CommerceShippingMethodsDisplayContext
			commerceShippingMethodsDisplayContext =
				new CommerceShippingMethodsDisplayContext(
					_commerceChannelLocalService,
					_commerceShippingEngineRegistry,
					_commerceShippingFixedOptionService,
					_commerceShippingMethodService, _countryService,
					httpServletRequest);

		httpServletRequest.setAttribute(
			WebKeys.PORTLET_DISPLAY_CONTEXT,
			commerceShippingMethodsDisplayContext);

		_jspRenderer.renderJSP(
			_servletContext, httpServletRequest, httpServletResponse,
			"/commerce_shipping_method/restrictions.jsp");
	}

	@Reference
	private CommerceChannelLocalService _commerceChannelLocalService;

	@Reference
	private CommerceShippingEngineRegistry _commerceShippingEngineRegistry;

	@Reference
	private CommerceShippingFixedOptionService
		_commerceShippingFixedOptionService;

	@Reference
	private CommerceShippingMethodService _commerceShippingMethodService;

	@Reference
	private CountryService _countryService;

	@Reference
	private JSPRenderer _jspRenderer;

	@Reference
	private Language _language;

	@Reference(
		target = "(osgi.web.symbolicname=com.liferay.commerce.shipping.web)"
	)
	private ServletContext _servletContext;

}