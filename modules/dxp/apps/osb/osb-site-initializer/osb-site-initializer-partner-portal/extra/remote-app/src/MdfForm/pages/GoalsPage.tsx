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

import {Form, Formik} from 'formik';
import {useState} from 'react';

import Button from '../../components/Button';
import CheckBoxList from '../../components/CheckBoxList';
import InputText from '../../components/InputText';
import Radio from '../../components/Radio';
import Select from '../../components/Select';
import LIST_TYPE_ENTRIES from '../../constants/listTypeEntries';
import {useGetListTypeDefinitions} from '../../services/list-type-definitions/useGetListTypeDefinitions';

const GoalsPage: any = ({
	setGeneralObject,
	setStep,
}: {
	setGeneralObject: any;
	setStep: any;
}) => {
	const businessSalesGoalPicklist = useGetListTypeDefinitions(
		LIST_TYPE_ENTRIES.businessSalesGoals
	);

	const goalsTargetMarketPicklist = useGetListTypeDefinitions(
		LIST_TYPE_ENTRIES.goalsTargetMarket
	);

	const targetAudienceRolePicklist = useGetListTypeDefinitions(
		LIST_TYPE_ENTRIES.targetAudienceRole
	);

	const [updateCheckBusines, setUpdateCheckBusines] = useState();
	const [updateGoalsTarget, setUpdateGoalsTarget] = useState();
	const [updateTargetAudience, setUpdateTargetAudience] = useState();

	const handleOnSubmit = (
		formData: any,
		setGeneralObject: any,
		updateCheckBusines: any,
		updateGoalsTarget: any,
		updateTargetAudience: any
	) => {
		const createForm = {
			activities: [],
			...formData,
			businessSalesGoals: updateCheckBusines,
			goalsTargetMarket: updateGoalsTarget,
			targetAudienceRole: updateTargetAudience,
		};

		setGeneralObject(createForm);
	};

	const optionsCompanyName = [
		{
			key: 'Deathray Parent-A*',
			name: 'Deathray Parent-A*',
		},
		{
			key: 'Deathray Parent-B*',
			name: 'Deathray Parent-B*',
		},
	];

	const optionsCountry = [
		{
			key: 'US',
			name: 'US',
		},
		{
			key: 'BR',
			name: 'BR',
		},
	];

	return (
		<Formik
			initialValues={{
				additionalOptions: '',
				businessSalesGoals: '',
				businessSalesGoalsOther: '',
				companyName: 'Deathray Parent-A*',
				country: 'US',
				goalsTargetMarket: '',
				provideNameAndDescription: '',
				targetAudienceRole: '',
			}}
			onSubmit={(formData) => {
				handleOnSubmit(
					formData,
					setGeneralObject,
					updateCheckBusines,
					updateGoalsTarget,
					updateTargetAudience
				);

				setStep(1);
			}}
		>
			{(formik) => (
				<div>
					<Form onSubmit={formik.handleSubmit}>
						<div className="border-0 mt-5 shadow-lg sheet sheet-lg">
							<div className="sheet-section">
								<div className="mb-4 sheet-header">
									<h5 className="text-primary">GOALS</h5>

									<h2>Campaign Information</h2>
								</div>

								<div className="mb-4 sheet-header">
									<h4>Partner</h4>
								</div>

								<div className="form-group-autofit">
									<div className="form-group-item">
										<Select
											label="Company Name"
											name="companyName"
											onChange={formik.handleChange}
											options={optionsCompanyName}
											value={formik.values.companyName}
										/>
									</div>

									<div className="form-group-item">
										<Select
											label="Country"
											name="country"
											onChange={formik.handleChange}
											options={optionsCountry}
											value={formik.values.country}
										/>
									</div>
								</div>

								<div className="mb-4 sheet-header">
									<h4>Campaign</h4>
								</div>

								<div className="form-group-autofit">
									<div className="form-group-item">
										<InputText
											className="form-control shadow-none"
											label="Provide a name and short description
											of the overall campaign"
											name="provideNameAndDescription"
											onChange={formik.handleChange}
											placeholder="Insurance Industry Lead Gen"
											type="text"
											value={
												formik.values
													.provideNameAndDescription
											}
										/>
									</div>
								</div>

								<div className="form-group-autofit">
									<div className="form-group-item">
										<label>
											Select Liferay business/sales goals
											this Campaign serves (choose up to
											three)
										</label>

										<div className="border border-light p-3 rounded">
											<CheckBoxList
												availableItems={
													businessSalesGoalPicklist
												}
												hasRule={true}
												maxCheckedItems={3}
												setCheckBox={
													setUpdateCheckBusines
												}
											/>

											<InputText
												className="form-control shadow-none"
												label=""
												name="businessSalesGoalsOther"
												onChange={formik.handleChange}
												placeholder="Input"
												type="text"
												value={
													formik.values
														.businessSalesGoalsOther
												}
											/>
										</div>
									</div>
								</div>

								<div className="mb-4 sheet-header">
									<h4>Target Market</h4>
								</div>

								<div className="form-group-autofit">
									<div className="form-group-item">
										<label>
											Please select the target market(s)
											for this campaign (choose up to
											three)
										</label>

										<div className="border border-light p-3 rounded">
											<>
												<CheckBoxList
													availableItems={
														goalsTargetMarketPicklist
													}
													hasRule={true}
													maxCheckedItems={3}
													setCheckBox={
														setUpdateGoalsTarget
													}
												/>
											</>
										</div>
									</div>
								</div>

								<div className="form-group-autofit">
									<div className="form-group-item">
										<label>
											Additional options? Choose one if
											applicable
										</label>

										<div className="border border-light mb-2 p-3 rounded">
											<Radio
												checked={
													formik.values
														.additionalOptions ===
													'liferayExperienceCloud'
												}
												inline
												label="Liferay Experience Cloud"
												name="additionalOptions"
												onChange={() => {
													formik.setFieldValue(
														'additionalOptions',
														'liferayExperienceCloud'
													);
												}}
												value="liferayExperienceCloud"
											/>
										</div>

										<div className="border border-light p-3 rounded">
											<div className="mb-1">
												<Radio
													checked={
														formik.values
															.additionalOptions ===
														'upgradeMigration'
													}
													inline
													label="Upgrade Migration"
													name="additionalOptions"
													onChange={() => {
														formik.setFieldValue(
															'additionalOptions',
															'upgradeMigration'
														);
													}}
													value="upgradeMigration"
												/>
											</div>

											<p className="ml-4 text-secondary">
												from competitor platform
											</p>
										</div>
									</div>
								</div>

								<div className="form-group-autofit">
									<div className="form-group-item">
										<label>
											Choose your target audience/role
											(Select all that apply)
										</label>

										<div className="border border-light p-3 rounded">
											<CheckBoxList
												availableItems={
													targetAudienceRolePicklist
												}
												hasRule={false}
												maxCheckedItems={3}
												setCheckBox={
													setUpdateTargetAudience
												}
											/>
										</div>
									</div>
								</div>
							</div>

							<div className="d-flex">
								<div className="mr-auto p-2">
									<Button
										className=""
										displayType="unstyled"
										icon=""
										label="Save as Draft"
										onClick={() => {}}
										type="button"
									/>
								</div>

								<div className="p-2">
									<Button
										className=""
										displayType="secondary"
										icon=""
										label="Cancel"
										onClick={() => {}}
										type="button"
									/>
								</div>

								<div className="p-2">
									<Button
										className=""
										displayType="primary"
										icon=""
										label="Continue"
										onClick={() => {}}
										type="submit"
									/>
								</div>
							</div>
						</div>
					</Form>
				</div>
			)}
		</Formik>
	);
};

export default GoalsPage;
