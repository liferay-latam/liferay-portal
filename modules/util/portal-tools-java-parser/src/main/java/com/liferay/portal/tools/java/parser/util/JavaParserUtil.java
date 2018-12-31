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

package com.liferay.portal.tools.java.parser.util;

import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.util.ArrayUtil;
import com.liferay.portal.kernel.util.Tuple;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.tools.java.parser.JavaAnnotation;
import com.liferay.portal.tools.java.parser.JavaAnnotationFieldDefinition;
import com.liferay.portal.tools.java.parser.JavaAnnotationMemberValuePair;
import com.liferay.portal.tools.java.parser.JavaArray;
import com.liferay.portal.tools.java.parser.JavaArrayDeclarator;
import com.liferay.portal.tools.java.parser.JavaArrayElement;
import com.liferay.portal.tools.java.parser.JavaBreakStatement;
import com.liferay.portal.tools.java.parser.JavaCatchStatement;
import com.liferay.portal.tools.java.parser.JavaClassCall;
import com.liferay.portal.tools.java.parser.JavaClassDefinition;
import com.liferay.portal.tools.java.parser.JavaConstructorCall;
import com.liferay.portal.tools.java.parser.JavaConstructorDefinition;
import com.liferay.portal.tools.java.parser.JavaContinueStatement;
import com.liferay.portal.tools.java.parser.JavaElseStatement;
import com.liferay.portal.tools.java.parser.JavaEnhancedForStatement;
import com.liferay.portal.tools.java.parser.JavaEnumConstantDefinition;
import com.liferay.portal.tools.java.parser.JavaEnumConstantDefinitions;
import com.liferay.portal.tools.java.parser.JavaExpression;
import com.liferay.portal.tools.java.parser.JavaForStatement;
import com.liferay.portal.tools.java.parser.JavaIfStatement;
import com.liferay.portal.tools.java.parser.JavaImport;
import com.liferay.portal.tools.java.parser.JavaInstanceofStatement;
import com.liferay.portal.tools.java.parser.JavaLambdaExpression;
import com.liferay.portal.tools.java.parser.JavaLambdaParameter;
import com.liferay.portal.tools.java.parser.JavaLoopStatement;
import com.liferay.portal.tools.java.parser.JavaMethodCall;
import com.liferay.portal.tools.java.parser.JavaMethodDefinition;
import com.liferay.portal.tools.java.parser.JavaMethodReference;
import com.liferay.portal.tools.java.parser.JavaNewArrayInstantiation;
import com.liferay.portal.tools.java.parser.JavaNewClassInstantiation;
import com.liferay.portal.tools.java.parser.JavaOperator;
import com.liferay.portal.tools.java.parser.JavaOperatorExpression;
import com.liferay.portal.tools.java.parser.JavaPackageDefinition;
import com.liferay.portal.tools.java.parser.JavaParameter;
import com.liferay.portal.tools.java.parser.JavaReturnStatement;
import com.liferay.portal.tools.java.parser.JavaSignature;
import com.liferay.portal.tools.java.parser.JavaSimpleValue;
import com.liferay.portal.tools.java.parser.JavaSwitchCaseStatement;
import com.liferay.portal.tools.java.parser.JavaSwitchStatement;
import com.liferay.portal.tools.java.parser.JavaSynchronizedStatement;
import com.liferay.portal.tools.java.parser.JavaTerm;
import com.liferay.portal.tools.java.parser.JavaTernaryOperator;
import com.liferay.portal.tools.java.parser.JavaThrowStatement;
import com.liferay.portal.tools.java.parser.JavaTryStatement;
import com.liferay.portal.tools.java.parser.JavaType;
import com.liferay.portal.tools.java.parser.JavaTypeCast;
import com.liferay.portal.tools.java.parser.JavaVariableDefinition;
import com.liferay.portal.tools.java.parser.JavaWhileStatement;

import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.api.FullIdent;
import com.puppycrawl.tools.checkstyle.api.TokenTypes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Hugo Huijser
 */
public class JavaParserUtil {

	public static JavaAnnotationFieldDefinition
		parseJavaAnnotationFieldDefinition(
			DetailAST annotationFieldDefinitionDetailAST) {

		JavaAnnotationFieldDefinition javaAnnotationFieldDefinition =
			new JavaAnnotationFieldDefinition(
				_parseJavaAnnotations(
					annotationFieldDefinitionDetailAST.findFirstToken(
						TokenTypes.MODIFIERS)),
				_parseJavaSignature(annotationFieldDefinitionDetailAST));

		DetailAST literalDefaultDetailAST =
			annotationFieldDefinitionDetailAST.findFirstToken(
				TokenTypes.LITERAL_DEFAULT);

		if (literalDefaultDetailAST != null) {
			javaAnnotationFieldDefinition.setDefaultJavaExpression(
				parseJavaExpression(literalDefaultDetailAST.getFirstChild()));
		}

		return javaAnnotationFieldDefinition;
	}

	public static JavaBreakStatement parseJavaBreakStatement(
		DetailAST literalBreakDetailAST) {

		JavaBreakStatement javaBreakStatement = new JavaBreakStatement();

		DetailAST firstChildDetailAST = literalBreakDetailAST.getFirstChild();

		if (firstChildDetailAST.getType() == TokenTypes.IDENT) {
			javaBreakStatement.setIdentifierName(firstChildDetailAST.getText());
		}

		return javaBreakStatement;
	}

	public static JavaCatchStatement parseJavaCatchStatement(
		DetailAST literalCatchDetailAST) {

		DetailAST parameterDefinitionDetailAST =
			literalCatchDetailAST.findFirstToken(TokenTypes.PARAMETER_DEF);

		List<JavaSimpleValue> modifiers = _parseModifiers(
			parameterDefinitionDetailAST.findFirstToken(TokenTypes.MODIFIERS));

		DetailAST identDetailAST = parameterDefinitionDetailAST.findFirstToken(
			TokenTypes.IDENT);

		String parameterName = identDetailAST.getText();

		List<JavaSimpleValue> parameterTypeNames = new ArrayList<>();

		DetailAST typeDetailAST = parameterDefinitionDetailAST.findFirstToken(
			TokenTypes.TYPE);

		DetailAST childDetailAST = typeDetailAST.getFirstChild();

		while (true) {
			DetailAST nextSiblingDetailAST = childDetailAST.getNextSibling();

			if (nextSiblingDetailAST != null) {
				FullIdent fullIdent = FullIdent.createFullIdent(
					nextSiblingDetailAST);

				parameterTypeNames.add(
					new JavaSimpleValue(fullIdent.getText()));
			}

			if (childDetailAST.getType() != TokenTypes.BOR) {
				FullIdent fullIdent = FullIdent.createFullIdent(childDetailAST);

				parameterTypeNames.add(
					new JavaSimpleValue(fullIdent.getText()));

				break;
			}

			childDetailAST = childDetailAST.getFirstChild();
		}

		if (parameterTypeNames.size() > 1) {
			Collections.reverse(parameterTypeNames);
		}

		return new JavaCatchStatement(
			modifiers, parameterName, parameterTypeNames);
	}

	public static JavaClassDefinition parseJavaClassDefinition(
		DetailAST definitionDetailAST) {

		DetailAST modifiersDetailAST = definitionDetailAST.findFirstToken(
			TokenTypes.MODIFIERS);

		String type = StringPool.BLANK;

		DetailAST nextSiblingDetailAST = modifiersDetailAST.getNextSibling();

		while (nextSiblingDetailAST.getType() != TokenTypes.IDENT) {
			type += nextSiblingDetailAST.getText();

			nextSiblingDetailAST = nextSiblingDetailAST.getNextSibling();
		}

		JavaType classJavaType = new JavaType(_getName(definitionDetailAST), 0);

		DetailAST typeParametersDetailAST = definitionDetailAST.findFirstToken(
			TokenTypes.TYPE_PARAMETERS);

		if (typeParametersDetailAST != null) {
			classJavaType.setGenericJavaTypes(
				_parseGenericJavaTypes(
					typeParametersDetailAST, TokenTypes.TYPE_PARAMETER));
		}

		JavaClassDefinition javaClassDefinition = new JavaClassDefinition(
			type, _parseJavaAnnotations(modifiersDetailAST),
			_parseModifiers(modifiersDetailAST), classJavaType);

		DetailAST extendsClauseDetailAST = definitionDetailAST.findFirstToken(
			TokenTypes.EXTENDS_CLAUSE);

		if (extendsClauseDetailAST != null) {
			javaClassDefinition.setExtendedClassJavaTypes(
				_parseExtendedOrImplementedClassJavaTypes(
					extendsClauseDetailAST));
		}

		DetailAST implementsClauseDetailAST =
			definitionDetailAST.findFirstToken(TokenTypes.IMPLEMENTS_CLAUSE);

		if (implementsClauseDetailAST != null) {
			javaClassDefinition.setImplementedClassJavaTypes(
				_parseExtendedOrImplementedClassJavaTypes(
					implementsClauseDetailAST));
		}

		return javaClassDefinition;
	}

	public static JavaConstructorCall parseJavaConstructorCall(
		DetailAST detailAST) {

		boolean superCall = false;

		if (detailAST.getType() == TokenTypes.SUPER_CTOR_CALL) {
			superCall = true;
		}

		return new JavaConstructorCall(
			_parseParameterValueJavaExpressions(
				detailAST.findFirstToken(TokenTypes.ELIST)),
			superCall);
	}

	public static JavaConstructorDefinition parseJavaConstructorDefinition(
		DetailAST constructorDefinitionDetailAST) {

		return new JavaConstructorDefinition(
			_parseJavaAnnotations(
				constructorDefinitionDetailAST.findFirstToken(
					TokenTypes.MODIFIERS)),
			_parseJavaSignature(constructorDefinitionDetailAST));
	}

	public static JavaContinueStatement parseJavaContinueStatement(
		DetailAST literalContinueDetailAST) {

		JavaContinueStatement javaContinueStatement =
			new JavaContinueStatement();

		DetailAST firstChildDetailAST =
			literalContinueDetailAST.getFirstChild();

		if (firstChildDetailAST.getType() == TokenTypes.IDENT) {
			javaContinueStatement.setIdentifierName(
				firstChildDetailAST.getText());
		}

		return javaContinueStatement;
	}

	public static JavaElseStatement parseJavaElseStatement(
		DetailAST literalElseDetailAST) {

		JavaElseStatement javaElseStatement = new JavaElseStatement();

		DetailAST firstChildDetailAST = literalElseDetailAST.getFirstChild();

		if (firstChildDetailAST.getType() == TokenTypes.LITERAL_IF) {
			javaElseStatement.setJavaIfStatement(
				parseJavaIfStatement(firstChildDetailAST));
		}

		return javaElseStatement;
	}

	public static JavaEnumConstantDefinitions parseJavaEnumConstantDefinitions(
		DetailAST enumConstantDefinitionDetailAST) {

		JavaEnumConstantDefinitions javaEnumConstantDefinitions =
			new JavaEnumConstantDefinitions();

		DetailAST detailAST = enumConstantDefinitionDetailAST;

		while (true) {
			if (detailAST == null) {
				return javaEnumConstantDefinitions;
			}

			if (detailAST.getType() == TokenTypes.ENUM_CONSTANT_DEF) {
				javaEnumConstantDefinitions.addJavaEnumConstantDefinition(
					_parseJavaEnumConstantDefinition(detailAST));
			}
			else if (detailAST.getType() != TokenTypes.COMMA) {
				return javaEnumConstantDefinitions;
			}

			detailAST = detailAST.getNextSibling();
		}
	}

	public static JavaExpression parseJavaExpression(DetailAST detailAST) {
		if (detailAST.getType() == TokenTypes.EXPR) {
			detailAST = detailAST.getFirstChild();
		}

		boolean hasSurroundingParentheses = false;

		while (true) {
			if (detailAST.getType() == TokenTypes.LPAREN) {
				detailAST = detailAST.getNextSibling();

				hasSurroundingParentheses = true;
			}
			else if (detailAST.getType() == TokenTypes.RPAREN) {
				detailAST = detailAST.getPreviousSibling();

				hasSurroundingParentheses = true;
			}
			else {
				break;
			}
		}

		JavaExpression javaExpression = null;

		if (detailAST.getType() == TokenTypes.ANNOTATION) {
			javaExpression = _parseJavaAnnotation(detailAST);
		}
		else if ((detailAST.getType() == TokenTypes.ANNOTATION_ARRAY_INIT) ||
				 (detailAST.getType() == TokenTypes.ARRAY_INIT)) {

			javaExpression = _parseJavaArray(detailAST);
		}
		else if (detailAST.getType() == TokenTypes.ARRAY_DECLARATOR) {
			javaExpression = _parseJavaArrayDeclarator(detailAST);
		}
		else if (detailAST.getType() == TokenTypes.DOT) {
			DetailAST lastChildDetailAST = detailAST.getLastChild();

			if (lastChildDetailAST.getChildCount() > 0) {
				DetailAST firstChildDetailAST = detailAST.getFirstChild();

				javaExpression = new JavaSimpleValue(
					firstChildDetailAST.getText());

				javaExpression.setChainedJavaExpression(
					parseJavaExpression(lastChildDetailAST));
			}
			else {
				Tuple chainTuple = _getChainTuple(detailAST);

				javaExpression = (JavaExpression)chainTuple.getObject(1);

				if (javaExpression != null) {
					javaExpression.setChainedJavaExpression(
						new JavaSimpleValue((String)chainTuple.getObject(0)));
				}
				else {
					javaExpression = new JavaSimpleValue(
						(String)chainTuple.getObject(0));
				}
			}
		}
		else if (detailAST.getType() == TokenTypes.INDEX_OP) {
			javaExpression = _parseJavaArrayElement(detailAST);
		}
		else if (detailAST.getType() == TokenTypes.LAMBDA) {
			javaExpression = _parseJavaLambdaExpression(detailAST);
		}
		else if (detailAST.getType() == TokenTypes.LITERAL_INSTANCEOF) {
			javaExpression = _parseJavaInstanceofStatement(detailAST);
		}
		else if (detailAST.getType() == TokenTypes.LITERAL_NEW) {
			DetailAST arrayDeclaratorDetailAST = detailAST.findFirstToken(
				TokenTypes.ARRAY_DECLARATOR);

			if (arrayDeclaratorDetailAST != null) {
				javaExpression = _parseJavaNewArrayInstantiation(detailAST);
			}
			else {
				DetailAST elistDetailAST = detailAST.findFirstToken(
					TokenTypes.ELIST);

				if (elistDetailAST != null) {
					javaExpression = _parseJavaNewClassInstantiation(detailAST);
				}
			}
		}
		else if (detailAST.getType() == TokenTypes.METHOD_CALL) {
			return _parseJavaMethodCall(detailAST);
		}
		else if (detailAST.getType() == TokenTypes.METHOD_REF) {
			javaExpression = _parseJavaMethodReference(detailAST);
		}
		else if (detailAST.getType() == TokenTypes.QUESTION) {
			javaExpression = _parseJavaTernaryOperator(detailAST);
		}
		else if (detailAST.getType() == TokenTypes.TYPECAST) {
			javaExpression = _parseJavaTypeCast(detailAST);
		}
		else if (ArrayUtil.contains(_SIMPLE_TYPES, detailAST.getType())) {
			javaExpression = new JavaSimpleValue(detailAST.getText());
		}
		else {
			for (JavaOperator operator : JavaOperator.values()) {
				if (operator.getType() == detailAST.getType()) {
					javaExpression = _parseJavaOperatorExpression(
						detailAST, operator);

					break;
				}
			}
		}

		if ((javaExpression != null) && hasSurroundingParentheses) {
			javaExpression.setHasSurroundingParentheses(true);
		}

		return javaExpression;
	}

	public static JavaLoopStatement parseJavaForStatement(
		DetailAST literalForDetailAST) {

		DetailAST firstChildDetailAST = literalForDetailAST.getFirstChild();

		DetailAST nextSiblingDetailAST = firstChildDetailAST.getNextSibling();

		if (nextSiblingDetailAST.getType() == TokenTypes.FOR_EACH_CLAUSE) {
			return _parseJavaEnhancedForStatement(nextSiblingDetailAST);
		}

		JavaForStatement javaForStatement = new JavaForStatement();

		List<JavaTerm> initializationJavaTerms = new ArrayList<>();

		DetailAST forInitDetailAST = literalForDetailAST.findFirstToken(
			TokenTypes.FOR_INIT);

		firstChildDetailAST = forInitDetailAST.getFirstChild();

		if (firstChildDetailAST != null) {
			if (firstChildDetailAST.getType() == TokenTypes.ELIST) {
				List<DetailAST> exprDetailASTList =
					DetailASTUtil.getAllChildTokens(
						firstChildDetailAST, false, TokenTypes.EXPR);

				for (DetailAST exprDetailAST : exprDetailASTList) {
					initializationJavaTerms.add(
						parseJavaExpression(exprDetailAST));
				}
			}
			else {
				List<DetailAST> variableDefinitionASTList =
					DetailASTUtil.getAllChildTokens(
						forInitDetailAST, false, TokenTypes.VARIABLE_DEF);

				for (DetailAST variableDefinitionDetailAST :
						variableDefinitionASTList) {

					initializationJavaTerms.add(
						parseJavaVariableDefinition(
							variableDefinitionDetailAST));
				}
			}
		}

		javaForStatement.setInitializationJavaTerms(initializationJavaTerms);

		DetailAST forConditionDetailAST = literalForDetailAST.findFirstToken(
			TokenTypes.FOR_CONDITION);

		DetailAST exprDetailAST = forConditionDetailAST.findFirstToken(
			TokenTypes.EXPR);

		if (exprDetailAST != null) {
			javaForStatement.setConditionJavaExpression(
				parseJavaExpression(exprDetailAST));
		}

		DetailAST forIteratorDetailAST = literalForDetailAST.findFirstToken(
			TokenTypes.FOR_ITERATOR);

		List<JavaExpression> iteratorJavaExpressions = new ArrayList<>();

		DetailAST elistDetailAST = forIteratorDetailAST.findFirstToken(
			TokenTypes.ELIST);

		if (elistDetailAST != null) {
			List<DetailAST> exprDetailASTList = DetailASTUtil.getAllChildTokens(
				elistDetailAST, false, TokenTypes.EXPR);

			for (DetailAST curExprDetailAST : exprDetailASTList) {
				iteratorJavaExpressions.add(
					parseJavaExpression(curExprDetailAST));
			}
		}

		javaForStatement.setIteratorJavaExpression(iteratorJavaExpressions);

		return javaForStatement;
	}

	public static JavaIfStatement parseJavaIfStatement(
		DetailAST literalIfDetailAST) {

		DetailAST firstChildDetailAST = literalIfDetailAST.getFirstChild();

		return new JavaIfStatement(
			parseJavaExpression(firstChildDetailAST.getNextSibling()));
	}

	public static JavaImport parseJavaImport(
		DetailAST importDetailAST, boolean isStatic) {

		return new JavaImport(_getName(importDetailAST), isStatic);
	}

	public static JavaLoopStatement parseJavaLabeledStatement(
		DetailAST labeledStatementDetailAST) {

		JavaLoopStatement javaLoopStatement = null;

		DetailAST firstChildDetailAST =
			labeledStatementDetailAST.getFirstChild();

		DetailAST nextSiblingDetailAST = firstChildDetailAST.getNextSibling();

		if (nextSiblingDetailAST.getType() == TokenTypes.LITERAL_FOR) {
			javaLoopStatement = parseJavaForStatement(nextSiblingDetailAST);
		}
		else if (nextSiblingDetailAST.getType() == TokenTypes.LITERAL_WHILE) {
			javaLoopStatement = parseJavaWhileStatement(nextSiblingDetailAST);
		}

		if (javaLoopStatement != null) {
			javaLoopStatement.setLabelName(firstChildDetailAST.getText());
		}

		return javaLoopStatement;
	}

	public static JavaMethodDefinition parseJavaMethodDefinition(
		DetailAST methodDefinitionDetailAST) {

		return new JavaMethodDefinition(
			_parseJavaAnnotations(
				methodDefinitionDetailAST.findFirstToken(TokenTypes.MODIFIERS)),
			_parseJavaSignature(methodDefinitionDetailAST));
	}

	public static JavaPackageDefinition parseJavaPackageDefinition(
		DetailAST packageDefinitionDetailAST) {

		return new JavaPackageDefinition(
			_getName(packageDefinitionDetailAST),
			_parseJavaAnnotations(
				packageDefinitionDetailAST.findFirstToken(
					TokenTypes.ANNOTATIONS)));
	}

	public static JavaReturnStatement parseJavaReturnStatement(
		DetailAST literalReturnDetailAST) {

		JavaReturnStatement javaReturnStatement = new JavaReturnStatement();

		DetailAST firstChildDetailAST = literalReturnDetailAST.getFirstChild();

		if (firstChildDetailAST.getType() != TokenTypes.SEMI) {
			javaReturnStatement.setReturnJavaExpression(
				parseJavaExpression(firstChildDetailAST));
		}

		return javaReturnStatement;
	}

	public static JavaSwitchCaseStatement parseJavaSwitchCaseStatement(
		DetailAST caseGroupDetailAST) {

		DetailAST firstChildDetailAST = caseGroupDetailAST.getFirstChild();

		if (firstChildDetailAST.getType() == TokenTypes.LITERAL_DEFAULT) {
			return new JavaSwitchCaseStatement(true);
		}

		JavaSwitchCaseStatement javaSwitchCaseStatement =
			new JavaSwitchCaseStatement(false);

		javaSwitchCaseStatement.setSwitchCaseJavaExpression(
			parseJavaExpression(firstChildDetailAST.getFirstChild()));

		return javaSwitchCaseStatement;
	}

	public static JavaSwitchStatement parseJavaSwitchStatement(
		DetailAST literalSwitchDetailAST) {

		DetailAST lparenDetailAST = literalSwitchDetailAST.getFirstChild();

		return new JavaSwitchStatement(
			parseJavaExpression(lparenDetailAST.getNextSibling()));
	}

	public static JavaSynchronizedStatement parseJavaSynchronizedStatement(
		DetailAST literalSynchronizedDetailAST) {

		JavaSynchronizedStatement javaSynchronizedStatement =
			new JavaSynchronizedStatement();

		DetailAST firstChildDetailAST =
			literalSynchronizedDetailAST.getFirstChild();

		javaSynchronizedStatement.setSynchronizedJavaExpression(
			parseJavaExpression(firstChildDetailAST.getNextSibling()));

		return javaSynchronizedStatement;
	}

	public static JavaThrowStatement parseJavaThrowStatement(
		DetailAST literalThrowDetailAST) {

		return new JavaThrowStatement(
			parseJavaExpression(literalThrowDetailAST.getFirstChild()));
	}

	public static JavaTryStatement parseJavaTryStatement(
		DetailAST literalTryDetailAST) {

		JavaTryStatement javaTryStatement = new JavaTryStatement();

		DetailAST firstChildDetailAST = literalTryDetailAST.getFirstChild();

		if (firstChildDetailAST.getType() !=
				TokenTypes.RESOURCE_SPECIFICATION) {

			return javaTryStatement;
		}

		List<JavaVariableDefinition> resourceJavaVariableDefinitions =
			new ArrayList<>();

		DetailAST resourcesDetailAST = firstChildDetailAST.findFirstToken(
			TokenTypes.RESOURCES);

		List<DetailAST> resourceDetailASTList = DetailASTUtil.getAllChildTokens(
			resourcesDetailAST, false, TokenTypes.RESOURCE);

		for (DetailAST resourceDetailAST : resourceDetailASTList) {
			resourceJavaVariableDefinitions.add(
				parseJavaVariableDefinition(resourceDetailAST));
		}

		javaTryStatement.setResourceJavaVariableDefinitions(
			resourceJavaVariableDefinitions);

		return javaTryStatement;
	}

	public static JavaVariableDefinition parseJavaVariableDefinition(
		DetailAST detailAST) {

		DetailAST modifiersDetailAST = detailAST.findFirstToken(
			TokenTypes.MODIFIERS);

		JavaVariableDefinition javaVariableDefinition =
			new JavaVariableDefinition(
				_getName(detailAST), _parseJavaAnnotations(modifiersDetailAST),
				_parseModifiers(modifiersDetailAST));

		javaVariableDefinition.setJavaType(
			_parseJavaType(detailAST.findFirstToken(TokenTypes.TYPE)));

		DetailAST assignDetailAST = detailAST.findFirstToken(TokenTypes.ASSIGN);

		if (assignDetailAST != null) {
			javaVariableDefinition.setAssignValueJavaExpression(
				parseJavaExpression(assignDetailAST.getFirstChild()));
		}

		return javaVariableDefinition;
	}

	public static JavaWhileStatement parseJavaWhileStatement(
		DetailAST detailAST) {

		DetailAST lparenDetailAST = null;

		if (detailAST.getType() == TokenTypes.LITERAL_WHILE) {
			lparenDetailAST = detailAST.getFirstChild();
		}
		else {
			lparenDetailAST = detailAST.getNextSibling();
		}

		return new JavaWhileStatement(
			parseJavaExpression(lparenDetailAST.getNextSibling()));
	}

	private static Tuple _getChainTuple(DetailAST dotDetailAST) {
		String name = StringPool.BLANK;

		DetailAST detailAST = dotDetailAST;

		while (true) {
			if (detailAST.getType() == TokenTypes.DOT) {
				DetailAST lastChildDetailAST = detailAST.getLastChild();

				if (Validator.isNull(name)) {
					name = lastChildDetailAST.getText();
				}
				else {
					name = lastChildDetailAST.getText() + "." + name;
				}

				detailAST = detailAST.getFirstChild();

				continue;
			}

			JavaExpression javaExpression = null;

			if (ArrayUtil.contains(_SIMPLE_TYPES, detailAST.getType()) &&
				(detailAST.getFirstChild() == null)) {

				name = detailAST.getText() + "." + name;
			}
			else {
				javaExpression = parseJavaExpression(detailAST);
			}

			return new Tuple(name, javaExpression);
		}
	}

	private static String _getName(DetailAST detailAST) {
		DetailAST identDetailAST = detailAST.findFirstToken(TokenTypes.IDENT);

		if (identDetailAST != null) {
			return identDetailAST.getText();
		}

		DetailAST firstChildDetailAST = detailAST.getFirstChild();

		if (ArrayUtil.contains(_SIMPLE_TYPES, firstChildDetailAST.getType())) {
			return firstChildDetailAST.getText();
		}

		DetailAST dotDetailAST = detailAST.findFirstToken(TokenTypes.DOT);

		FullIdent fullIdent = FullIdent.createFullIdent(dotDetailAST);

		return fullIdent.getText();
	}

	private static List<JavaExpression> _parseArrayValueJavaExpressions(
		DetailAST detailAST) {

		int bracketType = detailAST.getType();

		List<JavaExpression> arrayValueJavaExpressions = new ArrayList<>();

		DetailAST firstChildDetailAST = detailAST;

		while (true) {
			if (firstChildDetailAST.getType() != bracketType) {
				if (arrayValueJavaExpressions.size() > 1) {
					Collections.reverse(arrayValueJavaExpressions);
				}

				return arrayValueJavaExpressions;
			}

			DetailAST closeBracketDetailAST =
				firstChildDetailAST.findFirstToken(TokenTypes.RBRACK);

			DetailAST previousSiblingDetailAST =
				closeBracketDetailAST.getPreviousSibling();

			if ((previousSiblingDetailAST == null) ||
				(previousSiblingDetailAST.getType() == bracketType)) {

				arrayValueJavaExpressions.add(
					new JavaSimpleValue(StringPool.BLANK));
			}
			else {
				arrayValueJavaExpressions.add(
					parseJavaExpression(previousSiblingDetailAST));
			}

			firstChildDetailAST = firstChildDetailAST.getFirstChild();
		}
	}

	private static List<JavaExpression> _parseExceptionJavaExpressions(
		DetailAST throwsDetailAST) {

		List<JavaExpression> exceptionJavaExpressions = new ArrayList<>();

		if (throwsDetailAST == null) {
			return exceptionJavaExpressions;
		}

		DetailAST childDetailAST = throwsDetailAST.getFirstChild();

		while (true) {
			if (childDetailAST == null) {
				return exceptionJavaExpressions;
			}

			if (childDetailAST.getType() != TokenTypes.COMMA) {
				exceptionJavaExpressions.add(
					parseJavaExpression(childDetailAST));
			}

			childDetailAST = childDetailAST.getNextSibling();
		}
	}

	private static List<JavaType> _parseExtendedOrImplementedClassJavaTypes(
		DetailAST clauseDetailAST) {

		List<JavaType> classJavaTypes = new ArrayList<>();

		DetailAST childDetailAST = clauseDetailAST.getFirstChild();

		while (true) {
			if (childDetailAST == null) {
				return classJavaTypes;
			}

			if (childDetailAST.getType() == TokenTypes.IDENT) {
				JavaType javaType = new JavaType(childDetailAST.getText(), 0);

				DetailAST nextSiblingDetailAST =
					childDetailAST.getNextSibling();

				if ((nextSiblingDetailAST != null) &&
					(nextSiblingDetailAST.getType() ==
						TokenTypes.TYPE_ARGUMENTS)) {

					javaType.setGenericJavaTypes(
						_parseGenericJavaTypes(
							nextSiblingDetailAST, TokenTypes.TYPE_ARGUMENT));
				}

				classJavaTypes.add(javaType);
			}
			else if (childDetailAST.getType() == TokenTypes.DOT) {
				FullIdent fullIdent = FullIdent.createFullIdent(childDetailAST);

				JavaType javaType = new JavaType(fullIdent.getText(), 0);

				DetailAST typeArgumentsDetailAST =
					childDetailAST.findFirstToken(TokenTypes.TYPE_ARGUMENTS);

				if (typeArgumentsDetailAST != null) {
					javaType.setGenericJavaTypes(
						_parseGenericJavaTypes(
							typeArgumentsDetailAST, TokenTypes.TYPE_ARGUMENT));
				}

				classJavaTypes.add(javaType);
			}

			childDetailAST = childDetailAST.getNextSibling();
		}
	}

	private static JavaType _parseGenericBoundJavaType(DetailAST detailAST) {
		FullIdent fullIdent = FullIdent.createFullIdent(detailAST);

		JavaType genericBoundJavaType = new JavaType(fullIdent.getText(), 0);

		DetailAST typeArgumentsDetailAST = null;

		if (detailAST.getType() != TokenTypes.DOT) {
			typeArgumentsDetailAST = detailAST.getNextSibling();
		}
		else {
			typeArgumentsDetailAST = detailAST.getLastChild();
		}

		if ((typeArgumentsDetailAST != null) &&
			(typeArgumentsDetailAST.getType() == TokenTypes.TYPE_ARGUMENTS)) {

			genericBoundJavaType.setGenericJavaTypes(
				_parseGenericJavaTypes(
					typeArgumentsDetailAST, TokenTypes.TYPE_ARGUMENT));
		}

		return genericBoundJavaType;
	}

	private static List<JavaType> _parseGenericBoundJavaTypes(
		DetailAST detailAST) {

		List<JavaType> genericBoundJavaTypes = new ArrayList<>();

		DetailAST childDetailAST = detailAST.getFirstChild();

		while (true) {
			if (childDetailAST == null) {
				return genericBoundJavaTypes;
			}

			if ((childDetailAST.getType() != TokenTypes.TYPE_ARGUMENTS) &&
				(childDetailAST.getType() != TokenTypes.TYPE_EXTENSION_AND)) {

				genericBoundJavaTypes.add(
					_parseGenericBoundJavaType(childDetailAST));
			}

			childDetailAST = childDetailAST.getNextSibling();
		}
	}

	private static List<JavaType> _parseGenericJavaTypes(
		DetailAST groupDetailAST, int type) {

		if (groupDetailAST == null) {
			return null;
		}

		List<JavaType> genericJavaTypes = new ArrayList<>();

		List<DetailAST> detailAstList = DetailASTUtil.getAllChildTokens(
			groupDetailAST, false, type);

		for (DetailAST currentDetailAST : detailAstList) {
			DetailAST childDetailAST = currentDetailAST.getFirstChild();

			if (childDetailAST.getType() == TokenTypes.TYPE) {
				genericJavaTypes.add(_parseJavaType(childDetailAST));
			}
			else {
				genericJavaTypes.add(_parseJavaType(currentDetailAST));
			}
		}

		return genericJavaTypes;
	}

	private static JavaAnnotation _parseJavaAnnotation(
		DetailAST annotationDetailAST) {

		JavaAnnotation javaAnnotation = new JavaAnnotation(
			_getName(annotationDetailAST));

		DetailAST lparenDetailAST = annotationDetailAST.findFirstToken(
			TokenTypes.LPAREN);

		if (lparenDetailAST == null) {
			return javaAnnotation;
		}

		List<JavaAnnotationMemberValuePair> javaAnnotationMemberValuePairs =
			_parseJavaAnnotationMemberValuePairs(annotationDetailAST);

		if (!javaAnnotationMemberValuePairs.isEmpty()) {
			javaAnnotation.setJavaAnnotationMemberValuePairs(
				javaAnnotationMemberValuePairs);
		}
		else {
			javaAnnotation.setValueJavaExpression(
				parseJavaExpression(lparenDetailAST.getNextSibling()));
		}

		return javaAnnotation;
	}

	private static JavaAnnotationMemberValuePair
		_parseJavaAnnotationMemberValuePair(
			DetailAST annotationMemberValuePairDetailAST) {

		DetailAST identDetailAST =
			annotationMemberValuePairDetailAST.findFirstToken(TokenTypes.IDENT);

		return new JavaAnnotationMemberValuePair(
			identDetailAST.getText(),
			parseJavaExpression(
				annotationMemberValuePairDetailAST.getLastChild()));
	}

	private static List<JavaAnnotationMemberValuePair>
		_parseJavaAnnotationMemberValuePairs(DetailAST annotationDetailAST) {

		List<JavaAnnotationMemberValuePair> javaAnnotationMemberValuePairs =
			new ArrayList<>();

		List<DetailAST> annotationMemberValuePairDetailASTList =
			DetailASTUtil.getAllChildTokens(
				annotationDetailAST, false,
				TokenTypes.ANNOTATION_MEMBER_VALUE_PAIR);

		for (DetailAST annotationMemberValuePairDetailAST :
				annotationMemberValuePairDetailASTList) {

			javaAnnotationMemberValuePairs.add(
				_parseJavaAnnotationMemberValuePair(
					annotationMemberValuePairDetailAST));
		}

		return javaAnnotationMemberValuePairs;
	}

	private static List<JavaAnnotation> _parseJavaAnnotations(
		DetailAST detailAST) {

		List<JavaAnnotation> javaAnnotations = new ArrayList<>();

		List<DetailAST> annotationDetailASTList =
			DetailASTUtil.getAllChildTokens(
				detailAST, false, TokenTypes.ANNOTATION);

		for (DetailAST annotationDetailAST : annotationDetailASTList) {
			javaAnnotations.add(_parseJavaAnnotation(annotationDetailAST));
		}

		return javaAnnotations;
	}

	private static JavaArray _parseJavaArray(DetailAST arrayDetailAST) {
		JavaArray javaArray = new JavaArray();

		DetailAST childDetailAST = arrayDetailAST.getFirstChild();

		while (true) {
			if ((childDetailAST == null) ||
				(childDetailAST.getType() == TokenTypes.RCURLY)) {

				return javaArray;
			}

			javaArray.addValueJavaExpression(
				parseJavaExpression(childDetailAST));

			childDetailAST = childDetailAST.getNextSibling();
			childDetailAST = childDetailAST.getNextSibling();
		}
	}

	private static JavaArrayDeclarator _parseJavaArrayDeclarator(
		DetailAST arrayDeclaratorDetailAST) {

		List<JavaExpression> dimensionValueJavaExpressions = new ArrayList<>();

		dimensionValueJavaExpressions.add(
			new JavaSimpleValue(StringPool.BLANK));

		DetailAST childDetailAST = arrayDeclaratorDetailAST.getFirstChild();

		while (true) {
			if (childDetailAST.getType() != TokenTypes.ARRAY_DECLARATOR) {
				FullIdent fullIdent = FullIdent.createFullIdent(childDetailAST);

				return new JavaArrayDeclarator(
					fullIdent.getText(), dimensionValueJavaExpressions);
			}

			dimensionValueJavaExpressions.add(
				new JavaSimpleValue(StringPool.BLANK));

			childDetailAST = childDetailAST.getFirstChild();
		}
	}

	private static JavaArrayElement _parseJavaArrayElement(
		DetailAST indexOpDetailAST) {

		JavaExpression arrayJavaExpression = null;

		DetailAST firstChildDetailAST = indexOpDetailAST.getFirstChild();

		while (true) {
			if (firstChildDetailAST.getType() != TokenTypes.INDEX_OP) {
				arrayJavaExpression = parseJavaExpression(firstChildDetailAST);

				break;
			}

			firstChildDetailAST = firstChildDetailAST.getFirstChild();
		}

		return new JavaArrayElement(
			arrayJavaExpression,
			_parseArrayValueJavaExpressions(indexOpDetailAST));
	}

	private static JavaClassCall _parseJavaClassCall(
		DetailAST literalNewDetailAST) {

		DetailAST typeArgumentDetailAST = literalNewDetailAST.findFirstToken(
			TokenTypes.TYPE_ARGUMENTS);

		if (typeArgumentDetailAST == null) {
			DetailAST firstChildDetailAST = literalNewDetailAST.getFirstChild();

			if (firstChildDetailAST.getType() == TokenTypes.DOT) {
				typeArgumentDetailAST = firstChildDetailAST.findFirstToken(
					TokenTypes.TYPE_ARGUMENTS);
			}
		}

		JavaClassCall javaClassCall = new JavaClassCall(
			_getName(literalNewDetailAST),
			_parseGenericJavaTypes(
				typeArgumentDetailAST, TokenTypes.TYPE_ARGUMENT),
			_parseParameterValueJavaExpressions(
				literalNewDetailAST.findFirstToken(TokenTypes.ELIST)));

		DetailAST objBlockDetailAST = literalNewDetailAST.findFirstToken(
			TokenTypes.OBJBLOCK);

		if (objBlockDetailAST != null) {
			javaClassCall.setHasBody(true);

			if (objBlockDetailAST.getChildCount() == 2) {
				javaClassCall.setEmptyBody(true);
			}
		}

		return javaClassCall;
	}

	private static JavaEnhancedForStatement _parseJavaEnhancedForStatement(
		DetailAST forEachClauseDetailAST) {

		return new JavaEnhancedForStatement(
			parseJavaExpression(
				forEachClauseDetailAST.findFirstToken(TokenTypes.EXPR)),
			parseJavaVariableDefinition(
				forEachClauseDetailAST.findFirstToken(
					TokenTypes.VARIABLE_DEF)));
	}

	private static JavaEnumConstantDefinition _parseJavaEnumConstantDefinition(
		DetailAST enumConstantDefinitionDetailAST) {

		JavaEnumConstantDefinition javaEnumConstantDefinition =
			new JavaEnumConstantDefinition(
				_getName(enumConstantDefinitionDetailAST),
				_parseJavaAnnotations(
					enumConstantDefinitionDetailAST.findFirstToken(
						TokenTypes.ANNOTATIONS)));

		DetailAST elistDetailAST =
			enumConstantDefinitionDetailAST.findFirstToken(TokenTypes.ELIST);

		if (elistDetailAST != null) {
			javaEnumConstantDefinition.setParameterValueJavaExpressions(
				_parseParameterValueJavaExpressions(elistDetailAST));
		}

		return javaEnumConstantDefinition;
	}

	private static JavaInstanceofStatement _parseJavaInstanceofStatement(
		DetailAST literalInstanceofDetailAST) {

		DetailAST typeDetailAST = literalInstanceofDetailAST.findFirstToken(
			TokenTypes.TYPE);

		return new JavaInstanceofStatement(
			_parseJavaType(typeDetailAST),
			parseJavaExpression(literalInstanceofDetailAST.getFirstChild()));
	}

	private static JavaExpression _parseJavaLambdaExpression(
		DetailAST lambdaDetailAST) {

		JavaLambdaExpression javaLambdaExpression = null;

		DetailAST firstChildDetailAST = lambdaDetailAST.getFirstChild();

		if (firstChildDetailAST.getType() == TokenTypes.IDENT) {
			javaLambdaExpression = new JavaLambdaExpression(
				firstChildDetailAST.getText());
		}
		else {
			javaLambdaExpression = new JavaLambdaExpression(
				_parseJavaLambdaParameters(
					lambdaDetailAST.findFirstToken(TokenTypes.PARAMETERS)));
		}

		DetailAST lastChildDetailAST = lambdaDetailAST.getLastChild();

		if (lastChildDetailAST.getType() != TokenTypes.SLIST) {
			javaLambdaExpression.setLambdaActionJavaExpression(
				parseJavaExpression(lastChildDetailAST));
		}

		return javaLambdaExpression;
	}

	private static List<JavaLambdaParameter> _parseJavaLambdaParameters(
		DetailAST parametersDetailAST) {

		List<JavaLambdaParameter> javaLambdaParameters = new ArrayList<>();

		List<DetailAST> parameterDefinitionDetailASTList =
			DetailASTUtil.getAllChildTokens(
				parametersDetailAST, false, TokenTypes.PARAMETER_DEF);

		for (DetailAST parameterDefinitionDetailAST :
				parameterDefinitionDetailASTList) {

			JavaLambdaParameter javaLambdaParameter = new JavaLambdaParameter(
				_getName(parameterDefinitionDetailAST));

			DetailAST typeDetailAST =
				parameterDefinitionDetailAST.findFirstToken(TokenTypes.TYPE);

			if (typeDetailAST.getFirstChild() != null) {
				javaLambdaParameter.setJavaType(_parseJavaType(typeDetailAST));
			}

			javaLambdaParameters.add(javaLambdaParameter);
		}

		return javaLambdaParameters;
	}

	private static JavaExpression _parseJavaMethodCall(
		DetailAST methodCallDetailAST) {

		JavaExpression javaExpression = null;
		JavaMethodCall javaMethodCall = null;

		DetailAST firstChildDetailAST = methodCallDetailAST.getFirstChild();

		if (firstChildDetailAST.getType() == TokenTypes.IDENT) {
			javaMethodCall = new JavaMethodCall(firstChildDetailAST.getText());

			javaMethodCall.setGenericJavaTypes(
				_parseGenericJavaTypes(
					methodCallDetailAST.findFirstToken(
						TokenTypes.TYPE_ARGUMENTS),
					TokenTypes.TYPE_ARGUMENT));
		}
		else {
			Tuple chainTuple = _getChainTuple(firstChildDetailAST);

			String name = (String)chainTuple.getObject(0);

			javaExpression = (JavaExpression)chainTuple.getObject(1);

			if (javaExpression != null) {
				javaMethodCall = new JavaMethodCall(name);
			}
			else {
				int i = name.lastIndexOf(StringPool.PERIOD);

				if (i == -1) {
					javaMethodCall = new JavaMethodCall(name);
				}
				else {
					javaMethodCall = new JavaMethodCall(name.substring(i + 1));

					javaExpression = new JavaSimpleValue(name.substring(0, i));
				}
			}

			javaMethodCall.setGenericJavaTypes(
				_parseGenericJavaTypes(
					firstChildDetailAST.findFirstToken(
						TokenTypes.TYPE_ARGUMENTS),
					TokenTypes.TYPE_ARGUMENT));
		}

		javaMethodCall.setParameterValueJavaExpressions(
			_parseParameterValueJavaExpressions(
				methodCallDetailAST.findFirstToken(TokenTypes.ELIST)));

		if (javaExpression == null) {
			return javaMethodCall;
		}

		javaExpression.setChainedJavaExpression(javaMethodCall);

		return javaExpression;
	}

	private static JavaMethodReference _parseJavaMethodReference(
		DetailAST methodReferenceDetailAST) {

		DetailAST lastChildDetailAST = methodReferenceDetailAST.getLastChild();

		return new JavaMethodReference(
			lastChildDetailAST.getText(),
			parseJavaExpression(methodReferenceDetailAST.getFirstChild()),
			_parseGenericJavaTypes(
				methodReferenceDetailAST.findFirstToken(
					TokenTypes.TYPE_ARGUMENTS),
				TokenTypes.TYPE_ARGUMENT));
	}

	private static JavaNewArrayInstantiation _parseJavaNewArrayInstantiation(
		DetailAST literalNewDetailAST) {

		JavaNewArrayInstantiation javaNewArrayInstantiation =
			new JavaNewArrayInstantiation();

		JavaArrayDeclarator javaArrayDeclarator = new JavaArrayDeclarator(
			_getName(literalNewDetailAST),
			_parseArrayValueJavaExpressions(
				literalNewDetailAST.findFirstToken(
					TokenTypes.ARRAY_DECLARATOR)));

		javaArrayDeclarator.setGenericJavaTypes(
			_parseGenericJavaTypes(
				literalNewDetailAST.findFirstToken(TokenTypes.TYPE_ARGUMENTS),
				TokenTypes.TYPE_ARGUMENT));

		javaNewArrayInstantiation.setJavaArrayDeclarator(javaArrayDeclarator);

		DetailAST arrayInitDetailAST = literalNewDetailAST.findFirstToken(
			TokenTypes.ARRAY_INIT);

		if (arrayInitDetailAST != null) {
			javaNewArrayInstantiation.setInitialJavaArray(
				_parseJavaArray(arrayInitDetailAST));
		}

		return javaNewArrayInstantiation;
	}

	private static JavaNewClassInstantiation _parseJavaNewClassInstantiation(
		DetailAST literalNewDetailAST) {

		return new JavaNewClassInstantiation(
			_parseJavaClassCall(literalNewDetailAST));
	}

	private static JavaOperatorExpression _parseJavaOperatorExpression(
		DetailAST detailAST, JavaOperator javaOperator) {

		JavaOperatorExpression javaOperatorExpression =
			new JavaOperatorExpression(javaOperator);

		if (javaOperator.hasLeftHandExpression()) {
			javaOperatorExpression.setLeftHandJavaExpression(
				parseJavaExpression(detailAST.getFirstChild()));
		}

		if (javaOperator.hasRightHandExpression()) {
			javaOperatorExpression.setRightHandJavaExpression(
				parseJavaExpression(detailAST.getLastChild()));
		}

		return javaOperatorExpression;
	}

	private static JavaParameter _parseJavaParameter(
		DetailAST parameterDefinitionDetailAST) {

		DetailAST modifiersDetailAST =
			parameterDefinitionDetailAST.findFirstToken(TokenTypes.MODIFIERS);

		DetailAST typeDetailAST = parameterDefinitionDetailAST.findFirstToken(
			TokenTypes.TYPE);

		JavaType javaType = _parseJavaType(typeDetailAST);

		DetailAST ellipsisDetailAST =
			parameterDefinitionDetailAST.findFirstToken(TokenTypes.ELLIPSIS);

		if (ellipsisDetailAST != null) {
			javaType.setVarargs(true);
		}

		return new JavaParameter(
			_getName(parameterDefinitionDetailAST),
			_parseJavaAnnotations(modifiersDetailAST),
			_parseModifiers(modifiersDetailAST), javaType);
	}

	private static List<JavaParameter> _parseJavaParameters(
		DetailAST detailAST) {

		List<JavaParameter> javaParameters = new ArrayList<>();

		if (detailAST == null) {
			return javaParameters;
		}

		List<DetailAST> parameterDefinitionDetailASTList =
			DetailASTUtil.getAllChildTokens(
				detailAST, false, TokenTypes.PARAMETER_DEF);

		for (DetailAST parameterDefinitionDetailAST :
				parameterDefinitionDetailASTList) {

			javaParameters.add(
				_parseJavaParameter(parameterDefinitionDetailAST));
		}

		return javaParameters;
	}

	private static JavaSignature _parseJavaSignature(DetailAST detailAST) {
		DetailAST identDetailAST = detailAST.findFirstToken(TokenTypes.IDENT);
		DetailAST modifiersDetailAST = detailAST.findFirstToken(
			TokenTypes.MODIFIERS);

		return new JavaSignature(
			identDetailAST.getText(), _parseModifiers(modifiersDetailAST),
			_parseJavaType(detailAST.findFirstToken(TokenTypes.TYPE)),
			_parseGenericJavaTypes(
				detailAST.findFirstToken(TokenTypes.TYPE_PARAMETERS),
				TokenTypes.TYPE_PARAMETER),
			_parseJavaParameters(
				detailAST.findFirstToken(TokenTypes.PARAMETERS)),
			_parseExceptionJavaExpressions(
				detailAST.findFirstToken(TokenTypes.LITERAL_THROWS)));
	}

	private static JavaTernaryOperator _parseJavaTernaryOperator(
		DetailAST questionDetailAST) {

		DetailAST colonDetailAST = questionDetailAST.findFirstToken(
			TokenTypes.COLON);

		return new JavaTernaryOperator(
			parseJavaExpression(questionDetailAST.getFirstChild()),
			parseJavaExpression(colonDetailAST.getPreviousSibling()),
			parseJavaExpression(colonDetailAST.getNextSibling()));
	}

	private static JavaType _parseJavaType(DetailAST detailAST) {
		if (detailAST == null) {
			return null;
		}

		DetailAST childDetailAST = detailAST.getFirstChild();

		int arrayDimension = 0;

		while (childDetailAST.getType() == TokenTypes.ARRAY_DECLARATOR) {
			arrayDimension++;

			childDetailAST = childDetailAST.getFirstChild();
		}

		FullIdent typeIdent = FullIdent.createFullIdent(childDetailAST);

		JavaType javaType = new JavaType(typeIdent.getText(), arrayDimension);

		DetailAST typeInfoDetailAST = childDetailAST;

		if (childDetailAST.getType() != TokenTypes.DOT) {
			typeInfoDetailAST = childDetailAST.getParent();
		}

		javaType.setGenericJavaTypes(
			_parseGenericJavaTypes(
				typeInfoDetailAST.findFirstToken(TokenTypes.TYPE_ARGUMENTS),
				TokenTypes.TYPE_ARGUMENT));

		DetailAST typeLowerBoundsDetailAST = typeInfoDetailAST.findFirstToken(
			TokenTypes.TYPE_LOWER_BOUNDS);

		if (typeLowerBoundsDetailAST != null) {
			javaType.setLowerBoundJavaTypes(
				_parseGenericBoundJavaTypes(typeLowerBoundsDetailAST));
		}

		DetailAST typeUpperBoundsDetailAST = typeInfoDetailAST.findFirstToken(
			TokenTypes.TYPE_UPPER_BOUNDS);

		if (typeUpperBoundsDetailAST != null) {
			javaType.setUpperBoundJavaTypes(
				_parseGenericBoundJavaTypes(typeUpperBoundsDetailAST));
		}

		return javaType;
	}

	private static JavaTypeCast _parseJavaTypeCast(
		DetailAST typeCastDetailAST) {

		List<JavaType> javaTypes = new ArrayList<>();

		DetailAST firstChildDetailAST = typeCastDetailAST.getFirstChild();

		if (firstChildDetailAST.getType() == TokenTypes.TYPE) {
			javaTypes.add(_parseJavaType(firstChildDetailAST));
		}
		else {
			javaTypes.add(_parseJavaType(firstChildDetailAST.getFirstChild()));

			javaTypes.add(_parseJavaType(firstChildDetailAST.getLastChild()));
		}

		return new JavaTypeCast(
			javaTypes, parseJavaExpression(typeCastDetailAST.getLastChild()));
	}

	private static List<JavaSimpleValue> _parseModifiers(
		DetailAST modifiersDetailAST) {

		List<JavaSimpleValue> modifiers = new ArrayList<>();

		DetailAST childDetailAST = modifiersDetailAST.getFirstChild();

		while (true) {
			if (childDetailAST == null) {
				return modifiers;
			}

			if (childDetailAST.getType() != TokenTypes.ANNOTATION) {
				modifiers.add(new JavaSimpleValue(childDetailAST.getText()));
			}

			childDetailAST = childDetailAST.getNextSibling();
		}
	}

	private static List<JavaExpression> _parseParameterValueJavaExpressions(
		DetailAST elistDetailAST) {

		List<JavaExpression> parameterValueJavaExpressions = new ArrayList<>();

		DetailAST childDetailAST = elistDetailAST.getFirstChild();

		if (childDetailAST == null) {
			return parameterValueJavaExpressions;
		}

		while (true) {
			parameterValueJavaExpressions.add(
				parseJavaExpression(childDetailAST));

			childDetailAST = childDetailAST.getNextSibling();

			if (childDetailAST == null) {
				return parameterValueJavaExpressions;
			}

			childDetailAST = childDetailAST.getNextSibling();
		}
	}

	private static final int[] _SIMPLE_TYPES = {
		TokenTypes.CHAR_LITERAL, TokenTypes.IDENT, TokenTypes.LITERAL_BOOLEAN,
		TokenTypes.LITERAL_BYTE, TokenTypes.LITERAL_CHAR,
		TokenTypes.LITERAL_CLASS, TokenTypes.LITERAL_DOUBLE,
		TokenTypes.LITERAL_FALSE, TokenTypes.LITERAL_FLOAT,
		TokenTypes.LITERAL_INT, TokenTypes.LITERAL_LONG,
		TokenTypes.LITERAL_NULL, TokenTypes.LITERAL_SHORT,
		TokenTypes.LITERAL_SUPER, TokenTypes.LITERAL_TRUE,
		TokenTypes.LITERAL_THIS, TokenTypes.LITERAL_VOID, TokenTypes.NUM_DOUBLE,
		TokenTypes.NUM_FLOAT, TokenTypes.NUM_INT, TokenTypes.NUM_LONG,
		TokenTypes.STRING_LITERAL
	};

}