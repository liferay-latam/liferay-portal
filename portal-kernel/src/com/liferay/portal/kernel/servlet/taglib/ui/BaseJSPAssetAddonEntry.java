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

package com.liferay.portal.kernel.servlet.taglib.ui;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Julio Camarero
 */
public abstract class BaseJSPAssetAddonEntry extends BaseAssetAddonEntry {

	public abstract String getJspPath();

	public abstract ServletContext getServletContext();

	@Override
	public void include(
			HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse)
		throws IOException {

		ServletContext servletContext = getServletContext();

		RequestDispatcher requestDispatcher =
			servletContext.getRequestDispatcher(getJspPath());

		try {
			requestDispatcher.include(httpServletRequest, httpServletResponse);
		}
		catch (ServletException servletException) {
			_log.error(
				"Unable to include JSP " + getJspPath(), servletException);

			throw new IOException(
				"Unable to include " + getJspPath(), servletException);
		}
	}

	private static final Log _log = LogFactoryUtil.getLog(
		BaseJSPAssetAddonEntry.class);

}