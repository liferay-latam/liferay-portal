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

package com.liferay.dynamic.data.mapping.model.impl;

import com.liferay.dynamic.data.mapping.model.DDMFormInstance;
import com.liferay.dynamic.data.mapping.model.DDMFormInstanceModel;
import com.liferay.dynamic.data.mapping.model.DDMFormInstanceSoap;
import com.liferay.expando.kernel.model.ExpandoBridge;
import com.liferay.expando.kernel.util.ExpandoBridgeFactoryUtil;
import com.liferay.exportimport.kernel.lar.StagedModelType;
import com.liferay.petra.string.StringBundler;
import com.liferay.portal.kernel.bean.AutoEscapeBeanHandler;
import com.liferay.portal.kernel.exception.LocaleException;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.json.JSON;
import com.liferay.portal.kernel.model.CacheModel;
import com.liferay.portal.kernel.model.ModelWrapper;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.model.impl.BaseModelImpl;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.UserLocalServiceUtil;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.util.LocalizationUtil;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.kernel.util.ProxyUtil;
import com.liferay.portal.kernel.util.Validator;

import java.io.Serializable;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationHandler;

import java.sql.Types;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.BiConsumer;
import java.util.function.Function;

import org.osgi.annotation.versioning.ProviderType;

/**
 * The base model implementation for the DDMFormInstance service. Represents a row in the &quot;DDMFormInstance&quot; database table, with each column mapped to a property of this class.
 *
 * <p>
 * This implementation and its corresponding interface </code>DDMFormInstanceModel</code> exist only as a container for the default property accessors generated by ServiceBuilder. Helper methods and all application logic should be put in {@link DDMFormInstanceImpl}.
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see DDMFormInstanceImpl
 * @generated
 */
@JSON(strict = true)
@ProviderType
public class DDMFormInstanceModelImpl
	extends BaseModelImpl<DDMFormInstance> implements DDMFormInstanceModel {

	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this class directly. All methods that expect a ddm form instance model instance should use the <code>DDMFormInstance</code> interface instead.
	 */
	public static final String TABLE_NAME = "DDMFormInstance";

	public static final Object[][] TABLE_COLUMNS = {
		{"mvccVersion", Types.BIGINT}, {"uuid_", Types.VARCHAR},
		{"formInstanceId", Types.BIGINT}, {"groupId", Types.BIGINT},
		{"companyId", Types.BIGINT}, {"userId", Types.BIGINT},
		{"userName", Types.VARCHAR}, {"versionUserId", Types.BIGINT},
		{"versionUserName", Types.VARCHAR}, {"createDate", Types.TIMESTAMP},
		{"modifiedDate", Types.TIMESTAMP}, {"structureId", Types.BIGINT},
		{"version", Types.VARCHAR}, {"name", Types.VARCHAR},
		{"description", Types.VARCHAR}, {"settings_", Types.CLOB},
		{"lastPublishDate", Types.TIMESTAMP}
	};

	public static final Map<String, Integer> TABLE_COLUMNS_MAP =
		new HashMap<String, Integer>();

	static {
		TABLE_COLUMNS_MAP.put("mvccVersion", Types.BIGINT);
		TABLE_COLUMNS_MAP.put("uuid_", Types.VARCHAR);
		TABLE_COLUMNS_MAP.put("formInstanceId", Types.BIGINT);
		TABLE_COLUMNS_MAP.put("groupId", Types.BIGINT);
		TABLE_COLUMNS_MAP.put("companyId", Types.BIGINT);
		TABLE_COLUMNS_MAP.put("userId", Types.BIGINT);
		TABLE_COLUMNS_MAP.put("userName", Types.VARCHAR);
		TABLE_COLUMNS_MAP.put("versionUserId", Types.BIGINT);
		TABLE_COLUMNS_MAP.put("versionUserName", Types.VARCHAR);
		TABLE_COLUMNS_MAP.put("createDate", Types.TIMESTAMP);
		TABLE_COLUMNS_MAP.put("modifiedDate", Types.TIMESTAMP);
		TABLE_COLUMNS_MAP.put("structureId", Types.BIGINT);
		TABLE_COLUMNS_MAP.put("version", Types.VARCHAR);
		TABLE_COLUMNS_MAP.put("name", Types.VARCHAR);
		TABLE_COLUMNS_MAP.put("description", Types.VARCHAR);
		TABLE_COLUMNS_MAP.put("settings_", Types.CLOB);
		TABLE_COLUMNS_MAP.put("lastPublishDate", Types.TIMESTAMP);
	}

	public static final String TABLE_SQL_CREATE =
		"create table DDMFormInstance (mvccVersion LONG default 0 not null,uuid_ VARCHAR(75) null,formInstanceId LONG not null primary key,groupId LONG,companyId LONG,userId LONG,userName VARCHAR(75) null,versionUserId LONG,versionUserName VARCHAR(75) null,createDate DATE null,modifiedDate DATE null,structureId LONG,version VARCHAR(75) null,name STRING null,description STRING null,settings_ TEXT null,lastPublishDate DATE null)";

	public static final String TABLE_SQL_DROP = "drop table DDMFormInstance";

	public static final String ORDER_BY_JPQL =
		" ORDER BY ddmFormInstance.formInstanceId ASC";

	public static final String ORDER_BY_SQL =
		" ORDER BY DDMFormInstance.formInstanceId ASC";

	public static final String DATA_SOURCE = "liferayDataSource";

	public static final String SESSION_FACTORY = "liferaySessionFactory";

	public static final String TX_MANAGER = "liferayTransactionManager";

	public static final long COMPANYID_COLUMN_BITMASK = 1L;

	public static final long GROUPID_COLUMN_BITMASK = 2L;

	public static final long UUID_COLUMN_BITMASK = 4L;

	public static final long FORMINSTANCEID_COLUMN_BITMASK = 8L;

	public static void setEntityCacheEnabled(boolean entityCacheEnabled) {
		_entityCacheEnabled = entityCacheEnabled;
	}

	public static void setFinderCacheEnabled(boolean finderCacheEnabled) {
		_finderCacheEnabled = finderCacheEnabled;
	}

	/**
	 * Converts the soap model instance into a normal model instance.
	 *
	 * @param soapModel the soap model instance to convert
	 * @return the normal model instance
	 */
	public static DDMFormInstance toModel(DDMFormInstanceSoap soapModel) {
		if (soapModel == null) {
			return null;
		}

		DDMFormInstance model = new DDMFormInstanceImpl();

		model.setMvccVersion(soapModel.getMvccVersion());
		model.setUuid(soapModel.getUuid());
		model.setFormInstanceId(soapModel.getFormInstanceId());
		model.setGroupId(soapModel.getGroupId());
		model.setCompanyId(soapModel.getCompanyId());
		model.setUserId(soapModel.getUserId());
		model.setUserName(soapModel.getUserName());
		model.setVersionUserId(soapModel.getVersionUserId());
		model.setVersionUserName(soapModel.getVersionUserName());
		model.setCreateDate(soapModel.getCreateDate());
		model.setModifiedDate(soapModel.getModifiedDate());
		model.setStructureId(soapModel.getStructureId());
		model.setVersion(soapModel.getVersion());
		model.setName(soapModel.getName());
		model.setDescription(soapModel.getDescription());
		model.setSettings(soapModel.getSettings());
		model.setLastPublishDate(soapModel.getLastPublishDate());

		return model;
	}

	/**
	 * Converts the soap model instances into normal model instances.
	 *
	 * @param soapModels the soap model instances to convert
	 * @return the normal model instances
	 */
	public static List<DDMFormInstance> toModels(
		DDMFormInstanceSoap[] soapModels) {

		if (soapModels == null) {
			return null;
		}

		List<DDMFormInstance> models = new ArrayList<DDMFormInstance>(
			soapModels.length);

		for (DDMFormInstanceSoap soapModel : soapModels) {
			models.add(toModel(soapModel));
		}

		return models;
	}

	public DDMFormInstanceModelImpl() {
	}

	@Override
	public long getPrimaryKey() {
		return _formInstanceId;
	}

	@Override
	public void setPrimaryKey(long primaryKey) {
		setFormInstanceId(primaryKey);
	}

	@Override
	public Serializable getPrimaryKeyObj() {
		return _formInstanceId;
	}

	@Override
	public void setPrimaryKeyObj(Serializable primaryKeyObj) {
		setPrimaryKey(((Long)primaryKeyObj).longValue());
	}

	@Override
	public Class<?> getModelClass() {
		return DDMFormInstance.class;
	}

	@Override
	public String getModelClassName() {
		return DDMFormInstance.class.getName();
	}

	@Override
	public Map<String, Object> getModelAttributes() {
		Map<String, Object> attributes = new HashMap<String, Object>();

		Map<String, Function<DDMFormInstance, Object>>
			attributeGetterFunctions = getAttributeGetterFunctions();

		for (Map.Entry<String, Function<DDMFormInstance, Object>> entry :
				attributeGetterFunctions.entrySet()) {

			String attributeName = entry.getKey();
			Function<DDMFormInstance, Object> attributeGetterFunction =
				entry.getValue();

			attributes.put(
				attributeName,
				attributeGetterFunction.apply((DDMFormInstance)this));
		}

		attributes.put("entityCacheEnabled", isEntityCacheEnabled());
		attributes.put("finderCacheEnabled", isFinderCacheEnabled());

		return attributes;
	}

	@Override
	public void setModelAttributes(Map<String, Object> attributes) {
		Map<String, BiConsumer<DDMFormInstance, Object>>
			attributeSetterBiConsumers = getAttributeSetterBiConsumers();

		for (Map.Entry<String, Object> entry : attributes.entrySet()) {
			String attributeName = entry.getKey();

			BiConsumer<DDMFormInstance, Object> attributeSetterBiConsumer =
				attributeSetterBiConsumers.get(attributeName);

			if (attributeSetterBiConsumer != null) {
				attributeSetterBiConsumer.accept(
					(DDMFormInstance)this, entry.getValue());
			}
		}
	}

	public Map<String, Function<DDMFormInstance, Object>>
		getAttributeGetterFunctions() {

		return _attributeGetterFunctions;
	}

	public Map<String, BiConsumer<DDMFormInstance, Object>>
		getAttributeSetterBiConsumers() {

		return _attributeSetterBiConsumers;
	}

	private static Function<InvocationHandler, DDMFormInstance>
		_getProxyProviderFunction() {

		Class<?> proxyClass = ProxyUtil.getProxyClass(
			DDMFormInstance.class.getClassLoader(), DDMFormInstance.class,
			ModelWrapper.class);

		try {
			Constructor<DDMFormInstance> constructor =
				(Constructor<DDMFormInstance>)proxyClass.getConstructor(
					InvocationHandler.class);

			return invocationHandler -> {
				try {
					return constructor.newInstance(invocationHandler);
				}
				catch (ReflectiveOperationException roe) {
					throw new InternalError(roe);
				}
			};
		}
		catch (NoSuchMethodException nsme) {
			throw new InternalError(nsme);
		}
	}

	private static final Map<String, Function<DDMFormInstance, Object>>
		_attributeGetterFunctions;
	private static final Map<String, BiConsumer<DDMFormInstance, Object>>
		_attributeSetterBiConsumers;

	static {
		Map<String, Function<DDMFormInstance, Object>>
			attributeGetterFunctions =
				new LinkedHashMap<String, Function<DDMFormInstance, Object>>();
		Map<String, BiConsumer<DDMFormInstance, ?>> attributeSetterBiConsumers =
			new LinkedHashMap<String, BiConsumer<DDMFormInstance, ?>>();

		attributeGetterFunctions.put(
			"mvccVersion", DDMFormInstance::getMvccVersion);
		attributeSetterBiConsumers.put(
			"mvccVersion",
			(BiConsumer<DDMFormInstance, Long>)DDMFormInstance::setMvccVersion);
		attributeGetterFunctions.put("uuid", DDMFormInstance::getUuid);
		attributeSetterBiConsumers.put(
			"uuid",
			(BiConsumer<DDMFormInstance, String>)DDMFormInstance::setUuid);
		attributeGetterFunctions.put(
			"formInstanceId", DDMFormInstance::getFormInstanceId);
		attributeSetterBiConsumers.put(
			"formInstanceId",
			(BiConsumer<DDMFormInstance, Long>)
				DDMFormInstance::setFormInstanceId);
		attributeGetterFunctions.put("groupId", DDMFormInstance::getGroupId);
		attributeSetterBiConsumers.put(
			"groupId",
			(BiConsumer<DDMFormInstance, Long>)DDMFormInstance::setGroupId);
		attributeGetterFunctions.put(
			"companyId", DDMFormInstance::getCompanyId);
		attributeSetterBiConsumers.put(
			"companyId",
			(BiConsumer<DDMFormInstance, Long>)DDMFormInstance::setCompanyId);
		attributeGetterFunctions.put("userId", DDMFormInstance::getUserId);
		attributeSetterBiConsumers.put(
			"userId",
			(BiConsumer<DDMFormInstance, Long>)DDMFormInstance::setUserId);
		attributeGetterFunctions.put("userName", DDMFormInstance::getUserName);
		attributeSetterBiConsumers.put(
			"userName",
			(BiConsumer<DDMFormInstance, String>)DDMFormInstance::setUserName);
		attributeGetterFunctions.put(
			"versionUserId", DDMFormInstance::getVersionUserId);
		attributeSetterBiConsumers.put(
			"versionUserId",
			(BiConsumer<DDMFormInstance, Long>)
				DDMFormInstance::setVersionUserId);
		attributeGetterFunctions.put(
			"versionUserName", DDMFormInstance::getVersionUserName);
		attributeSetterBiConsumers.put(
			"versionUserName",
			(BiConsumer<DDMFormInstance, String>)
				DDMFormInstance::setVersionUserName);
		attributeGetterFunctions.put(
			"createDate", DDMFormInstance::getCreateDate);
		attributeSetterBiConsumers.put(
			"createDate",
			(BiConsumer<DDMFormInstance, Date>)DDMFormInstance::setCreateDate);
		attributeGetterFunctions.put(
			"modifiedDate", DDMFormInstance::getModifiedDate);
		attributeSetterBiConsumers.put(
			"modifiedDate",
			(BiConsumer<DDMFormInstance, Date>)
				DDMFormInstance::setModifiedDate);
		attributeGetterFunctions.put(
			"structureId", DDMFormInstance::getStructureId);
		attributeSetterBiConsumers.put(
			"structureId",
			(BiConsumer<DDMFormInstance, Long>)DDMFormInstance::setStructureId);
		attributeGetterFunctions.put("version", DDMFormInstance::getVersion);
		attributeSetterBiConsumers.put(
			"version",
			(BiConsumer<DDMFormInstance, String>)DDMFormInstance::setVersion);
		attributeGetterFunctions.put("name", DDMFormInstance::getName);
		attributeSetterBiConsumers.put(
			"name",
			(BiConsumer<DDMFormInstance, String>)DDMFormInstance::setName);
		attributeGetterFunctions.put(
			"description", DDMFormInstance::getDescription);
		attributeSetterBiConsumers.put(
			"description",
			(BiConsumer<DDMFormInstance, String>)
				DDMFormInstance::setDescription);
		attributeGetterFunctions.put("settings", DDMFormInstance::getSettings);
		attributeSetterBiConsumers.put(
			"settings",
			(BiConsumer<DDMFormInstance, String>)DDMFormInstance::setSettings);
		attributeGetterFunctions.put(
			"lastPublishDate", DDMFormInstance::getLastPublishDate);
		attributeSetterBiConsumers.put(
			"lastPublishDate",
			(BiConsumer<DDMFormInstance, Date>)
				DDMFormInstance::setLastPublishDate);

		_attributeGetterFunctions = Collections.unmodifiableMap(
			attributeGetterFunctions);
		_attributeSetterBiConsumers = Collections.unmodifiableMap(
			(Map)attributeSetterBiConsumers);
	}

	@JSON
	@Override
	public long getMvccVersion() {
		return _mvccVersion;
	}

	@Override
	public void setMvccVersion(long mvccVersion) {
		_mvccVersion = mvccVersion;
	}

	@JSON
	@Override
	public String getUuid() {
		if (_uuid == null) {
			return "";
		}
		else {
			return _uuid;
		}
	}

	@Override
	public void setUuid(String uuid) {
		_columnBitmask |= UUID_COLUMN_BITMASK;

		if (_originalUuid == null) {
			_originalUuid = _uuid;
		}

		_uuid = uuid;
	}

	public String getOriginalUuid() {
		return GetterUtil.getString(_originalUuid);
	}

	@JSON
	@Override
	public long getFormInstanceId() {
		return _formInstanceId;
	}

	@Override
	public void setFormInstanceId(long formInstanceId) {
		_formInstanceId = formInstanceId;
	}

	@JSON
	@Override
	public long getGroupId() {
		return _groupId;
	}

	@Override
	public void setGroupId(long groupId) {
		_columnBitmask |= GROUPID_COLUMN_BITMASK;

		if (!_setOriginalGroupId) {
			_setOriginalGroupId = true;

			_originalGroupId = _groupId;
		}

		_groupId = groupId;
	}

	public long getOriginalGroupId() {
		return _originalGroupId;
	}

	@JSON
	@Override
	public long getCompanyId() {
		return _companyId;
	}

	@Override
	public void setCompanyId(long companyId) {
		_columnBitmask |= COMPANYID_COLUMN_BITMASK;

		if (!_setOriginalCompanyId) {
			_setOriginalCompanyId = true;

			_originalCompanyId = _companyId;
		}

		_companyId = companyId;
	}

	public long getOriginalCompanyId() {
		return _originalCompanyId;
	}

	@JSON
	@Override
	public long getUserId() {
		return _userId;
	}

	@Override
	public void setUserId(long userId) {
		_userId = userId;
	}

	@Override
	public String getUserUuid() {
		try {
			User user = UserLocalServiceUtil.getUserById(getUserId());

			return user.getUuid();
		}
		catch (PortalException pe) {
			return "";
		}
	}

	@Override
	public void setUserUuid(String userUuid) {
	}

	@JSON
	@Override
	public String getUserName() {
		if (_userName == null) {
			return "";
		}
		else {
			return _userName;
		}
	}

	@Override
	public void setUserName(String userName) {
		_userName = userName;
	}

	@JSON
	@Override
	public long getVersionUserId() {
		return _versionUserId;
	}

	@Override
	public void setVersionUserId(long versionUserId) {
		_versionUserId = versionUserId;
	}

	@Override
	public String getVersionUserUuid() {
		try {
			User user = UserLocalServiceUtil.getUserById(getVersionUserId());

			return user.getUuid();
		}
		catch (PortalException pe) {
			return "";
		}
	}

	@Override
	public void setVersionUserUuid(String versionUserUuid) {
	}

	@JSON
	@Override
	public String getVersionUserName() {
		if (_versionUserName == null) {
			return "";
		}
		else {
			return _versionUserName;
		}
	}

	@Override
	public void setVersionUserName(String versionUserName) {
		_versionUserName = versionUserName;
	}

	@JSON
	@Override
	public Date getCreateDate() {
		return _createDate;
	}

	@Override
	public void setCreateDate(Date createDate) {
		_createDate = createDate;
	}

	@JSON
	@Override
	public Date getModifiedDate() {
		return _modifiedDate;
	}

	public boolean hasSetModifiedDate() {
		return _setModifiedDate;
	}

	@Override
	public void setModifiedDate(Date modifiedDate) {
		_setModifiedDate = true;

		_modifiedDate = modifiedDate;
	}

	@JSON
	@Override
	public long getStructureId() {
		return _structureId;
	}

	@Override
	public void setStructureId(long structureId) {
		_structureId = structureId;
	}

	@JSON
	@Override
	public String getVersion() {
		if (_version == null) {
			return "";
		}
		else {
			return _version;
		}
	}

	@Override
	public void setVersion(String version) {
		_version = version;
	}

	@JSON
	@Override
	public String getName() {
		if (_name == null) {
			return "";
		}
		else {
			return _name;
		}
	}

	@Override
	public String getName(Locale locale) {
		String languageId = LocaleUtil.toLanguageId(locale);

		return getName(languageId);
	}

	@Override
	public String getName(Locale locale, boolean useDefault) {
		String languageId = LocaleUtil.toLanguageId(locale);

		return getName(languageId, useDefault);
	}

	@Override
	public String getName(String languageId) {
		return LocalizationUtil.getLocalization(getName(), languageId);
	}

	@Override
	public String getName(String languageId, boolean useDefault) {
		return LocalizationUtil.getLocalization(
			getName(), languageId, useDefault);
	}

	@Override
	public String getNameCurrentLanguageId() {
		return _nameCurrentLanguageId;
	}

	@JSON
	@Override
	public String getNameCurrentValue() {
		Locale locale = getLocale(_nameCurrentLanguageId);

		return getName(locale);
	}

	@Override
	public Map<Locale, String> getNameMap() {
		return LocalizationUtil.getLocalizationMap(getName());
	}

	@Override
	public void setName(String name) {
		_name = name;
	}

	@Override
	public void setName(String name, Locale locale) {
		setName(name, locale, LocaleUtil.getSiteDefault());
	}

	@Override
	public void setName(String name, Locale locale, Locale defaultLocale) {
		String languageId = LocaleUtil.toLanguageId(locale);
		String defaultLanguageId = LocaleUtil.toLanguageId(defaultLocale);

		if (Validator.isNotNull(name)) {
			setName(
				LocalizationUtil.updateLocalization(
					getName(), "Name", name, languageId, defaultLanguageId));
		}
		else {
			setName(
				LocalizationUtil.removeLocalization(
					getName(), "Name", languageId));
		}
	}

	@Override
	public void setNameCurrentLanguageId(String languageId) {
		_nameCurrentLanguageId = languageId;
	}

	@Override
	public void setNameMap(Map<Locale, String> nameMap) {
		setNameMap(nameMap, LocaleUtil.getSiteDefault());
	}

	@Override
	public void setNameMap(Map<Locale, String> nameMap, Locale defaultLocale) {
		if (nameMap == null) {
			return;
		}

		setName(
			LocalizationUtil.updateLocalization(
				nameMap, getName(), "Name",
				LocaleUtil.toLanguageId(defaultLocale)));
	}

	@JSON
	@Override
	public String getDescription() {
		if (_description == null) {
			return "";
		}
		else {
			return _description;
		}
	}

	@Override
	public String getDescription(Locale locale) {
		String languageId = LocaleUtil.toLanguageId(locale);

		return getDescription(languageId);
	}

	@Override
	public String getDescription(Locale locale, boolean useDefault) {
		String languageId = LocaleUtil.toLanguageId(locale);

		return getDescription(languageId, useDefault);
	}

	@Override
	public String getDescription(String languageId) {
		return LocalizationUtil.getLocalization(getDescription(), languageId);
	}

	@Override
	public String getDescription(String languageId, boolean useDefault) {
		return LocalizationUtil.getLocalization(
			getDescription(), languageId, useDefault);
	}

	@Override
	public String getDescriptionCurrentLanguageId() {
		return _descriptionCurrentLanguageId;
	}

	@JSON
	@Override
	public String getDescriptionCurrentValue() {
		Locale locale = getLocale(_descriptionCurrentLanguageId);

		return getDescription(locale);
	}

	@Override
	public Map<Locale, String> getDescriptionMap() {
		return LocalizationUtil.getLocalizationMap(getDescription());
	}

	@Override
	public void setDescription(String description) {
		_description = description;
	}

	@Override
	public void setDescription(String description, Locale locale) {
		setDescription(description, locale, LocaleUtil.getSiteDefault());
	}

	@Override
	public void setDescription(
		String description, Locale locale, Locale defaultLocale) {

		String languageId = LocaleUtil.toLanguageId(locale);
		String defaultLanguageId = LocaleUtil.toLanguageId(defaultLocale);

		if (Validator.isNotNull(description)) {
			setDescription(
				LocalizationUtil.updateLocalization(
					getDescription(), "Description", description, languageId,
					defaultLanguageId));
		}
		else {
			setDescription(
				LocalizationUtil.removeLocalization(
					getDescription(), "Description", languageId));
		}
	}

	@Override
	public void setDescriptionCurrentLanguageId(String languageId) {
		_descriptionCurrentLanguageId = languageId;
	}

	@Override
	public void setDescriptionMap(Map<Locale, String> descriptionMap) {
		setDescriptionMap(descriptionMap, LocaleUtil.getSiteDefault());
	}

	@Override
	public void setDescriptionMap(
		Map<Locale, String> descriptionMap, Locale defaultLocale) {

		if (descriptionMap == null) {
			return;
		}

		setDescription(
			LocalizationUtil.updateLocalization(
				descriptionMap, getDescription(), "Description",
				LocaleUtil.toLanguageId(defaultLocale)));
	}

	@JSON
	@Override
	public String getSettings() {
		if (_settings == null) {
			return "";
		}
		else {
			return _settings;
		}
	}

	@Override
	public void setSettings(String settings) {
		_settings = settings;
	}

	@JSON
	@Override
	public Date getLastPublishDate() {
		return _lastPublishDate;
	}

	@Override
	public void setLastPublishDate(Date lastPublishDate) {
		_lastPublishDate = lastPublishDate;
	}

	public com.liferay.dynamic.data.mapping.storage.DDMFormValues
		getDDMFormValues() {

		return null;
	}

	public void setDDMFormValues(
		com.liferay.dynamic.data.mapping.storage.DDMFormValues ddmFormValues) {
	}

	@Override
	public StagedModelType getStagedModelType() {
		return new StagedModelType(
			PortalUtil.getClassNameId(DDMFormInstance.class.getName()));
	}

	public long getColumnBitmask() {
		return _columnBitmask;
	}

	@Override
	public ExpandoBridge getExpandoBridge() {
		return ExpandoBridgeFactoryUtil.getExpandoBridge(
			getCompanyId(), DDMFormInstance.class.getName(), getPrimaryKey());
	}

	@Override
	public void setExpandoBridgeAttributes(ServiceContext serviceContext) {
		ExpandoBridge expandoBridge = getExpandoBridge();

		expandoBridge.setAttributes(serviceContext);
	}

	@Override
	public String[] getAvailableLanguageIds() {
		Set<String> availableLanguageIds = new TreeSet<String>();

		Map<Locale, String> nameMap = getNameMap();

		for (Map.Entry<Locale, String> entry : nameMap.entrySet()) {
			Locale locale = entry.getKey();
			String value = entry.getValue();

			if (Validator.isNotNull(value)) {
				availableLanguageIds.add(LocaleUtil.toLanguageId(locale));
			}
		}

		Map<Locale, String> descriptionMap = getDescriptionMap();

		for (Map.Entry<Locale, String> entry : descriptionMap.entrySet()) {
			Locale locale = entry.getKey();
			String value = entry.getValue();

			if (Validator.isNotNull(value)) {
				availableLanguageIds.add(LocaleUtil.toLanguageId(locale));
			}
		}

		return availableLanguageIds.toArray(
			new String[availableLanguageIds.size()]);
	}

	@Override
	public String getDefaultLanguageId() {
		String xml = getName();

		if (xml == null) {
			return "";
		}

		Locale defaultLocale = LocaleUtil.getSiteDefault();

		return LocalizationUtil.getDefaultLanguageId(xml, defaultLocale);
	}

	@Override
	public void prepareLocalizedFieldsForImport() throws LocaleException {
		Locale defaultLocale = LocaleUtil.fromLanguageId(
			getDefaultLanguageId());

		Locale[] availableLocales = LocaleUtil.fromLanguageIds(
			getAvailableLanguageIds());

		Locale defaultImportLocale = LocalizationUtil.getDefaultImportLocale(
			DDMFormInstance.class.getName(), getPrimaryKey(), defaultLocale,
			availableLocales);

		prepareLocalizedFieldsForImport(defaultImportLocale);
	}

	@Override
	@SuppressWarnings("unused")
	public void prepareLocalizedFieldsForImport(Locale defaultImportLocale)
		throws LocaleException {

		Locale defaultLocale = LocaleUtil.getSiteDefault();

		String modelDefaultLanguageId = getDefaultLanguageId();

		String name = getName(defaultLocale);

		if (Validator.isNull(name)) {
			setName(getName(modelDefaultLanguageId), defaultLocale);
		}
		else {
			setName(getName(defaultLocale), defaultLocale, defaultLocale);
		}

		String description = getDescription(defaultLocale);

		if (Validator.isNull(description)) {
			setDescription(
				getDescription(modelDefaultLanguageId), defaultLocale);
		}
		else {
			setDescription(
				getDescription(defaultLocale), defaultLocale, defaultLocale);
		}
	}

	@Override
	public DDMFormInstance toEscapedModel() {
		if (_escapedModel == null) {
			Function<InvocationHandler, DDMFormInstance>
				escapedModelProxyProviderFunction =
					EscapedModelProxyProviderFunctionHolder.
						_escapedModelProxyProviderFunction;

			_escapedModel = escapedModelProxyProviderFunction.apply(
				new AutoEscapeBeanHandler(this));
		}

		return _escapedModel;
	}

	@Override
	public Object clone() {
		DDMFormInstanceImpl ddmFormInstanceImpl = new DDMFormInstanceImpl();

		ddmFormInstanceImpl.setMvccVersion(getMvccVersion());
		ddmFormInstanceImpl.setUuid(getUuid());
		ddmFormInstanceImpl.setFormInstanceId(getFormInstanceId());
		ddmFormInstanceImpl.setGroupId(getGroupId());
		ddmFormInstanceImpl.setCompanyId(getCompanyId());
		ddmFormInstanceImpl.setUserId(getUserId());
		ddmFormInstanceImpl.setUserName(getUserName());
		ddmFormInstanceImpl.setVersionUserId(getVersionUserId());
		ddmFormInstanceImpl.setVersionUserName(getVersionUserName());
		ddmFormInstanceImpl.setCreateDate(getCreateDate());
		ddmFormInstanceImpl.setModifiedDate(getModifiedDate());
		ddmFormInstanceImpl.setStructureId(getStructureId());
		ddmFormInstanceImpl.setVersion(getVersion());
		ddmFormInstanceImpl.setName(getName());
		ddmFormInstanceImpl.setDescription(getDescription());
		ddmFormInstanceImpl.setSettings(getSettings());
		ddmFormInstanceImpl.setLastPublishDate(getLastPublishDate());

		ddmFormInstanceImpl.resetOriginalValues();

		return ddmFormInstanceImpl;
	}

	@Override
	public int compareTo(DDMFormInstance ddmFormInstance) {
		long primaryKey = ddmFormInstance.getPrimaryKey();

		if (getPrimaryKey() < primaryKey) {
			return -1;
		}
		else if (getPrimaryKey() > primaryKey) {
			return 1;
		}
		else {
			return 0;
		}
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (!(obj instanceof DDMFormInstance)) {
			return false;
		}

		DDMFormInstance ddmFormInstance = (DDMFormInstance)obj;

		long primaryKey = ddmFormInstance.getPrimaryKey();

		if (getPrimaryKey() == primaryKey) {
			return true;
		}
		else {
			return false;
		}
	}

	@Override
	public int hashCode() {
		return (int)getPrimaryKey();
	}

	@Override
	public boolean isEntityCacheEnabled() {
		return _entityCacheEnabled;
	}

	@Override
	public boolean isFinderCacheEnabled() {
		return _finderCacheEnabled;
	}

	@Override
	public void resetOriginalValues() {
		DDMFormInstanceModelImpl ddmFormInstanceModelImpl = this;

		ddmFormInstanceModelImpl._originalUuid = ddmFormInstanceModelImpl._uuid;

		ddmFormInstanceModelImpl._originalGroupId =
			ddmFormInstanceModelImpl._groupId;

		ddmFormInstanceModelImpl._setOriginalGroupId = false;

		ddmFormInstanceModelImpl._originalCompanyId =
			ddmFormInstanceModelImpl._companyId;

		ddmFormInstanceModelImpl._setOriginalCompanyId = false;

		ddmFormInstanceModelImpl._setModifiedDate = false;

		setDDMFormValues(null);

		ddmFormInstanceModelImpl._columnBitmask = 0;
	}

	@Override
	public CacheModel<DDMFormInstance> toCacheModel() {
		DDMFormInstanceCacheModel ddmFormInstanceCacheModel =
			new DDMFormInstanceCacheModel();

		ddmFormInstanceCacheModel.mvccVersion = getMvccVersion();

		ddmFormInstanceCacheModel.uuid = getUuid();

		String uuid = ddmFormInstanceCacheModel.uuid;

		if ((uuid != null) && (uuid.length() == 0)) {
			ddmFormInstanceCacheModel.uuid = null;
		}

		ddmFormInstanceCacheModel.formInstanceId = getFormInstanceId();

		ddmFormInstanceCacheModel.groupId = getGroupId();

		ddmFormInstanceCacheModel.companyId = getCompanyId();

		ddmFormInstanceCacheModel.userId = getUserId();

		ddmFormInstanceCacheModel.userName = getUserName();

		String userName = ddmFormInstanceCacheModel.userName;

		if ((userName != null) && (userName.length() == 0)) {
			ddmFormInstanceCacheModel.userName = null;
		}

		ddmFormInstanceCacheModel.versionUserId = getVersionUserId();

		ddmFormInstanceCacheModel.versionUserName = getVersionUserName();

		String versionUserName = ddmFormInstanceCacheModel.versionUserName;

		if ((versionUserName != null) && (versionUserName.length() == 0)) {
			ddmFormInstanceCacheModel.versionUserName = null;
		}

		Date createDate = getCreateDate();

		if (createDate != null) {
			ddmFormInstanceCacheModel.createDate = createDate.getTime();
		}
		else {
			ddmFormInstanceCacheModel.createDate = Long.MIN_VALUE;
		}

		Date modifiedDate = getModifiedDate();

		if (modifiedDate != null) {
			ddmFormInstanceCacheModel.modifiedDate = modifiedDate.getTime();
		}
		else {
			ddmFormInstanceCacheModel.modifiedDate = Long.MIN_VALUE;
		}

		ddmFormInstanceCacheModel.structureId = getStructureId();

		ddmFormInstanceCacheModel.version = getVersion();

		String version = ddmFormInstanceCacheModel.version;

		if ((version != null) && (version.length() == 0)) {
			ddmFormInstanceCacheModel.version = null;
		}

		ddmFormInstanceCacheModel.name = getName();

		String name = ddmFormInstanceCacheModel.name;

		if ((name != null) && (name.length() == 0)) {
			ddmFormInstanceCacheModel.name = null;
		}

		ddmFormInstanceCacheModel.description = getDescription();

		String description = ddmFormInstanceCacheModel.description;

		if ((description != null) && (description.length() == 0)) {
			ddmFormInstanceCacheModel.description = null;
		}

		ddmFormInstanceCacheModel.settings = getSettings();

		String settings = ddmFormInstanceCacheModel.settings;

		if ((settings != null) && (settings.length() == 0)) {
			ddmFormInstanceCacheModel.settings = null;
		}

		Date lastPublishDate = getLastPublishDate();

		if (lastPublishDate != null) {
			ddmFormInstanceCacheModel.lastPublishDate =
				lastPublishDate.getTime();
		}
		else {
			ddmFormInstanceCacheModel.lastPublishDate = Long.MIN_VALUE;
		}

		ddmFormInstanceCacheModel._ddmFormValues = getDDMFormValues();

		return ddmFormInstanceCacheModel;
	}

	@Override
	public String toString() {
		Map<String, Function<DDMFormInstance, Object>>
			attributeGetterFunctions = getAttributeGetterFunctions();

		StringBundler sb = new StringBundler(
			4 * attributeGetterFunctions.size() + 2);

		sb.append("{");

		for (Map.Entry<String, Function<DDMFormInstance, Object>> entry :
				attributeGetterFunctions.entrySet()) {

			String attributeName = entry.getKey();
			Function<DDMFormInstance, Object> attributeGetterFunction =
				entry.getValue();

			sb.append(attributeName);
			sb.append("=");
			sb.append(attributeGetterFunction.apply((DDMFormInstance)this));
			sb.append(", ");
		}

		if (sb.index() > 1) {
			sb.setIndex(sb.index() - 1);
		}

		sb.append("}");

		return sb.toString();
	}

	@Override
	public String toXmlString() {
		Map<String, Function<DDMFormInstance, Object>>
			attributeGetterFunctions = getAttributeGetterFunctions();

		StringBundler sb = new StringBundler(
			5 * attributeGetterFunctions.size() + 4);

		sb.append("<model><model-name>");
		sb.append(getModelClassName());
		sb.append("</model-name>");

		for (Map.Entry<String, Function<DDMFormInstance, Object>> entry :
				attributeGetterFunctions.entrySet()) {

			String attributeName = entry.getKey();
			Function<DDMFormInstance, Object> attributeGetterFunction =
				entry.getValue();

			sb.append("<column><column-name>");
			sb.append(attributeName);
			sb.append("</column-name><column-value><![CDATA[");
			sb.append(attributeGetterFunction.apply((DDMFormInstance)this));
			sb.append("]]></column-value></column>");
		}

		sb.append("</model>");

		return sb.toString();
	}

	private static class EscapedModelProxyProviderFunctionHolder {

		private static final Function<InvocationHandler, DDMFormInstance>
			_escapedModelProxyProviderFunction = _getProxyProviderFunction();

	}

	private static boolean _entityCacheEnabled;
	private static boolean _finderCacheEnabled;

	private long _mvccVersion;
	private String _uuid;
	private String _originalUuid;
	private long _formInstanceId;
	private long _groupId;
	private long _originalGroupId;
	private boolean _setOriginalGroupId;
	private long _companyId;
	private long _originalCompanyId;
	private boolean _setOriginalCompanyId;
	private long _userId;
	private String _userName;
	private long _versionUserId;
	private String _versionUserName;
	private Date _createDate;
	private Date _modifiedDate;
	private boolean _setModifiedDate;
	private long _structureId;
	private String _version;
	private String _name;
	private String _nameCurrentLanguageId;
	private String _description;
	private String _descriptionCurrentLanguageId;
	private String _settings;
	private Date _lastPublishDate;
	private long _columnBitmask;
	private DDMFormInstance _escapedModel;

}