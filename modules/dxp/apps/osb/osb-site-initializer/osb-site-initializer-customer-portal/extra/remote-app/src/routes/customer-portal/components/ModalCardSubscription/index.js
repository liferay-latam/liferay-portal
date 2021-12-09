import {useQuery} from '@apollo/client';
import ClayButton from '@clayui/button';
import ClayIcon from '@clayui/icon';
import ClayModal from '@clayui/modal';
import React, {useMemo, useState} from 'react';
import Table from '../../../../common/components/Table';
import {getAccountSubscriptionsTerms} from '../../../../common/services/liferay/graphql/queries';
import {status} from '../../utils/constants';
import StatusTag from '../StatusTag';

const ModalCardSubscription = ({accountSubscriptionERC, observer, onClose}) => {
	const [activePage, setActivePage] = useState(1);
	const MAX_ITEMS_PER_PAGE = 5;

	const parseDate = (rawDate) => {
		const date = new Date(rawDate);
		const month = date.getMonth() + 1;
		const day = date.getDate();
		const year = date.getFullYear();

		return !rawDate
			? ''
			: `${day}/${month < 10 ? `0${month}` : month}/${year}`;
	};

	const {data: subscriptionsTerms} = useQuery(getAccountSubscriptionsTerms, {
		variables: {
			filter: `accountSubscriptionERC eq '${accountSubscriptionERC}'`,
			page: activePage,
			pageSize: MAX_ITEMS_PER_PAGE,
		},
	});

	const accountSubscriptionTermsItems =
		subscriptionsTerms?.c?.accountSubscriptionTerms?.items;
	const totalCount =
		subscriptionsTerms?.c?.accountSubscriptionTerms?.totalCount;

	const columns = useMemo(
		() => [
			{
				Cell: (props) => {
					return (
						<>{`${parseDate(props.startDate)} - ${parseDate(
							props.endDate
						)}`}</>
					);
				},
				Header: 'Start-End Date',
				accessor: 'start-end-date',
				align: 'center',
				expanded: true,
			},
			{
				Cell: (props) => {
					return <>{props?.provisioned || '-'}</>;
				},
				Header: 'Provisioned',
				accessor: 'provisioned',
				align: 'center',
			},
			{
				Cell: (props) => {
					return <>{props?.quantity || '-'}</>;
				},
				Header: 'Purchased',
				accessor: 'quantity',
				align: 'center',
			},
			{
				Cell: (props) => {
					return <>{props?.instanceSize || '-'}</>;
				},
				Header: 'Instance Size',
				accessor: 'instanceSize',
				align: 'center',
			},
			{
				Cell: (props) => {
					return (
						<>
							{(props?.subscriptionTermStatus && (
								<StatusTag
									currentStatus={
										status[
											`${props.subscriptionTermStatus.toLowerCase()}`
										]
									}
								/>
							)) ||
								'-'}
						</>
					);
				},
				Header: 'Status',
				accessor: 'subscriptionTermStatus',
				align: 'center',
			},
		],
		[]
	);

	return (
		<>
			<ClayModal center={true} observer={observer} size="lg">
				<div className="pt-4 px-4">
					<div className="d-flex justify-content-between mb-4 teste-cursor">
						<div className="flex-row mb-1">
							<h6 className="text-brand-primary">
								SUBSCRIPTION TERMS
							</h6>

							<h2 className="text-neutral-10">DXP Production</h2>
						</div>

						<ClayButton
							aria-label="close"
							className="close"
							displayType="unstyled"
							onClick={onClose}
						>
							<ClayIcon symbol="times" />
						</ClayButton>
					</div>

					<div>
						<Table
							activePage={activePage}
							columns={columns}
							data={accountSubscriptionTermsItems}
							hasPagination={totalCount >= 5}
							itemsPerPage={MAX_ITEMS_PER_PAGE}
							setActivePage={setActivePage}
							tableVerticalAlignment="middle"
							totalCount={totalCount}
						/>
					</div>
				</div>
			</ClayModal>
		</>
	);
};

export default ModalCardSubscription;
