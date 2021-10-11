import {createContext, useReducer} from 'react';
import FormProvider from '~/shared/providers/FormProvider';
import {
	getInitialDxpAdmin,
	getInitialInvite,
	roles,
	steps,
} from '../utils/constants';
import reducer from './reducer';

const initialApp = {
	dxp: {
		organization: 'SuperBank',
		version: '7.3',
	},
	step: steps.welcome,
};

const initialForm = {
	dxp: {
		admins: [getInitialDxpAdmin()],
		dataCenterRegion: '',
		projectId: '',
	},
	invites: [
		getInitialInvite(roles.creator.id),
		getInitialInvite(roles.watcher.id),
		getInitialInvite(roles.watcher.id),
	],
};

const AppContext = createContext();

const AppProvider = ({children}) => {
	const [state, dispatch] = useReducer(reducer, initialApp);

	return (
		<AppContext.Provider value={[state, dispatch]}>
			<FormProvider initialValues={initialForm}>{children}</FormProvider>
		</AppContext.Provider>
	);
};

export {AppContext, AppProvider};
