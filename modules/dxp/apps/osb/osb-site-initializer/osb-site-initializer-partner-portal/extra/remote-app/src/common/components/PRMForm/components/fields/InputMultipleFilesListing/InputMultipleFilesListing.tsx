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

import PRMForm from '../../..';
import PRMFormik from '../../../../PRMFormik';
import ListFiles from './components/ListFiles';

interface IProps {
	description: string;
	fieldValue: string;
	files?: File[];
	label: string;
	onAccept: (value: File[]) => void;
}

const InputMultipleFilesListing = ({
	description,
	fieldValue,
	files,
	label,
	onAccept,
}: IProps) => {
	return (
		<>
			<PRMFormik.Field
				component={PRMForm.InputMultipleFiles}
				description={description}
				label={label}
				onAccept={onAccept}
			/>
			<PRMFormik.Array
				component={ListFiles}
				files={files}
				name={fieldValue}
			/>
		</>
	);
};

export default InputMultipleFilesListing;
