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

import PortletBase from './PortletBase.es';
import delegate from './delegate/delegate.es';

/**
 * Appends list item elements to dropdown menus with inline-scrollers on scroll
 * events to improve page loading performance.
 *
 * @extends {Component}
 */
class DynamicInlineScroll extends PortletBase {
	constructor(config) {
		super();

		this.cur = Number(config.cur);
		this.curParam = config.curParam;
		this.forcePost = config.forcePost;
		this.formName = config.formName;
		this.initialPages = Number(config.initialPages);
		this.jsCall = config.jsCall;
		this.namespace = config.namespace;
		this.pages = Number(config.pages);
		this.randomNamespace = config.randomNamespace;
		this.url = config.url;
		this.urlAnchor = config.urlAnchor;
	}

	/**
	 * @inheritDoc
	 */
	attached() {
		let {rootNode} = this;

		rootNode = rootNode || document;

		this.inlineScrollEventHandler_ = delegate(
			rootNode,
			'scroll',
			'ul.pagination ul.inline-scroller',
			this.onScroll_.bind(this)
		);
	}

	/**
	 * @inheritDoc
	 */
	created() {
		this.handleListItemClick_ = this.handleListItemClick_.bind(this);
	}

	/**
	 * @inheritDoc
	 */
	detached() {
		super.detached();

		this.inlineScrollEventHandler_.dispose();

		const listItem = document.createElement('li');

		listItem.removeEventListener('click', this.handleListItemClick_);
	}

	/**
	 * Dynamically adds list item elements to the dropdown menu.
	 *
	 * @param {element} listElement The list element's DOM node.
	 * @param {number} pageIndex The Index of the page with an inline-scroller.
	 * @protected
	 */
	addListItem_(listElement, pageIndex) {
		const listItem = document.createElement('li');

		listItem.innerHTML = `<a class="dropdown-item" href="${this.getHREF_(
			pageIndex
		)}">${pageIndex}</a>`;

		pageIndex++;

		listElement.appendChild(listItem);
		listElement.setAttribute('data-page-index', pageIndex);

		listItem.addEventListener('click', this.handleListItemClick_);
	}

	/**
	 * Returns the <code>href</code> attribute value for each page.
	 *
	 * @param {number} pageIndex The Index of the page.
	 * @protected
	 * @return {string} The <code>href</code> value as a string.
	 */
	getHREF_(pageIndex) {
		const {curParam, formName, jsCall, namespace, url, urlAnchor} = this;

		let href = `javascript:document.${formName}.${namespace}${curParam}.value = "${pageIndex}; ${jsCall}`;

		if (this.url !== null) {
			href = `${url}&${namespace}${curParam}=${pageIndex}${urlAnchor}`;
		}

		return href;
	}

	/**
	 * Handles the click event of the dynmaically added list item, preventing
	 * the default behavior and submitting the search container form.
	 *
	 * @param {Event} event The click event of the dynamically added list item.
	 * @protected
	 */
	handleListItemClick_(event) {
		if (this.forcePost) {
			event.preventDefault();

			const {curParam, namespace, randomNamespace} = this;

			const form = document.getElementById(
				randomNamespace + namespace + 'pageIteratorFm'
			);

			form.elements[namespace + curParam].value =
				event.currentTarget.textContent;

			form.submit();
		}
	}

	/**
	 * An event triggered when a dropdown menu with an inline-scroller is
	 * scrolled. This dynamically adds list item elements to the dropdown menu
	 * as it is scrolled down.
	 *
	 * @param {Event} event The scroll event triggered by scrolling a dropdown
	 *        menu with an inline-scroller.
	 * @protected
	 */
	onScroll_(event) {
		const {cur, initialPages, pages} = this;
		const {target} = event;

		let pageIndex = Number(target.dataset.pageIndex);
		let pageIndexMax = Number(target.dataset.maxIndex);

		if (pageIndex === 0) {
			const pageIndexCurrent = Number(target.dataset.currentIndex);

			if (pageIndexCurrent === 0) {
				pageIndex = initialPages;
			}
			else {
				pageIndex = pageIndexCurrent + initialPages;
			}
		}

		if (pageIndexMax === 0) {
			pageIndexMax = pages;
		}

		if (
			cur <= pages &&
			pageIndex < pageIndexMax &&
			target.getAttribute('scrollTop') >=
				target.getAttribute('scrollHeight') - 300
		) {
			this.addListItem_(target, pageIndex);
		}
	}
}

export default DynamicInlineScroll;
