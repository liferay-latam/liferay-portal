/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * The contents of this file are subject to the terms of the Liferay Enterprise
 * Subscription License ("License"). You may not use this file except in
 * compliance with the License. You can obtain a copy of the License by
 * contacting Liferay, Inc. See the License for the specific language governing
 * permissions and limitations under the License, including but not limited to
 * distribution rights of the Software.
 */
import {useEffect} from 'react';
import {useOutletContext} from 'react-router-dom';
import i18n from '../../../../../common/I18n';
import {useGetLiferayExperienceCloudEnvironments} from '../../../../../common/services/liferay/graphql/liferay-experience-cloud-environments/';
import ActivationStatus from '../../../components/ActivationStatus';
import {useCustomerPortal} from '../../../context';
import {PRODUCT_TYPES} from '../../../utils/constants';

const LiferayExperienceCloud = () => {
	const [
		{project, subscriptionGroups, userAccount},
		dispatch,
	] = useCustomerPortal();
	const {setHasQuickLinksPanel, setHasSideMenu} = useOutletContext();

	useEffect(() => {
		setHasQuickLinksPanel(true);
		setHasSideMenu(true);
	}, [setHasSideMenu, setHasQuickLinksPanel]);

	const {data} = useGetLiferayExperienceCloudEnvironments({
		filter: `accountKey eq '${project?.accountKey}'`,
	});

	const liferayExperienceCloudEnvironment =
		data?.c?.liferayExperienceCloudEnvironments?.items[0];

	const subscriptionGroupLxcEnvironment = subscriptionGroups?.find(
		(subscriptionGroup) =>
			subscriptionGroup.name === PRODUCT_TYPES.liferayExperienceCloud
	);

	if (!project || !subscriptionGroups) {
		return <> {i18n.translate('loading')}...</>;
	}

	return (
		<div>
			<ActivationStatus.LiferayExperienceCloud
				data={data}
				dispatch={dispatch}
				lxcEnvironment={liferayExperienceCloudEnvironment}
				project={project}
				subscriptionGroupLxcEnvironment={
					subscriptionGroupLxcEnvironment
				}
				subscriptionGroups={subscriptionGroups}
				userAccount={userAccount}
			/>
		</div>
	);
};

export default LiferayExperienceCloud;
