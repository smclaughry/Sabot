package org.sabot.server.inject.module;

import static com.google.common.collect.Sets.filter;
import static com.google.inject.matcher.Matchers.any;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import javax.inject.Inject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Predicate;
import com.google.common.collect.Sets;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.ProvisionException;
import com.google.inject.TypeLiteral;
import com.google.inject.spi.InjectionListener;
import com.google.inject.spi.TypeEncounter;
import com.google.inject.spi.TypeListener;

/*
 * Library Sabot: a library for accelerating GWT and AppEngine development
 * 
 * Copyright (C) 2011  Phil Craven, Stephen McLaughry
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 */
public class BindJakartaCommonsLoggingLoggerWithClassNameModule extends AbstractModule {
	@Override
	protected void configure() {
		bindListener(any(), new BindLoggers());
	}

	@Provides
	Log provideDefaultLoggerToSatisfyGuiceProvisionCheck() {
		return LogFactory.getLog("");
	}

	@VisibleForTesting
	static class BindLoggers implements TypeListener {

		static class AssignLoggerToField<I> implements InjectionListener<I> {
			private final Log logger;
			private final Field field;

			AssignLoggerToField(Log logger, Field field) {
				this.logger = logger;
				this.field = field;
			}

			public void afterInjection(I injectee) {
				try {
					field.setAccessible(true);
					field.set(injectee, logger);
				} catch (IllegalAccessException e) {
					throw new ProvisionException(e.getMessage(), e);
				}
			}
		}

		public <I> void hear(TypeLiteral<I> injectableType, TypeEncounter<I> encounter) {
			Class<? super I> type = injectableType.getRawType();
			Set<Field> loggerFields;
			if (hasInjectableConstructorWithLoggerParameter(type)){
				loggerFields = getAllLoggerFieldsFrom(type);
			}else{
				loggerFields = getInjectableLoggerFieldsFrom(type);
			}
			if (loggerFields.isEmpty()){
				return;
			}
			// assign the correct scope to the logger based on the classname
			Log logger = getCorrectLoggerForType(type);

			assignLoggerAfterInjection(encounter, loggerFields, logger);
		}

		@VisibleForTesting
		static Set<Field> getInjectableLoggerFieldsFrom(Class<?> type) {
			return onlyInjectableFields(onlyLoggerFields(allFieldsFrom(type)));
		}

		@VisibleForTesting
		static Set<Field> getAllLoggerFieldsFrom(Class<?> type) {
			return onlyLoggerFields(allFieldsFrom(type));
		}

		private static Log getCorrectLoggerForType(Class<?> type) {
			return LogFactory.getLog(type.getName());
		}

		private static <I> void assignLoggerAfterInjection(TypeEncounter<I> encounter,
				Set<Field> loggerFields, Log logger) {
			for (Field field : loggerFields) {
				encounter.register(new AssignLoggerToField<I>(logger, field));
			}
		}

		@VisibleForTesting
		static boolean hasInjectableConstructorWithLoggerParameter(Class<?> declaredType) {
			// iterate through class and superclass constructors.
			Class<?> type = declaredType;
			while (type != null) {
				for (Constructor<?> constructor : type.getConstructors()) {
					// only inject guiced constructors
					if (!constructor.isAnnotationPresent(Inject.class)){
						continue;
					}
					if (hasLoggerParameter(constructor)){
						return true;
					}
				}
				type = type.getSuperclass();
			}
			return false;
		}

		private static boolean hasLoggerParameter(Constructor<?> constructor) {
			return filter(Sets.newHashSet(constructor.getParameterTypes()), new Predicate<Class<?>>() {
				public boolean apply(Class<?> input) {
					return input.isAssignableFrom(Log.class);
				}
			}).size() > 0;
		}

		private static Set<Field> allFieldsFrom(Class<?> declaredType) {
			Set<Field> fields = new HashSet<Field>();
			// find all the fields for this type.
			Class<?> type = declaredType;
			while (type != null) {
				fields.addAll(Arrays.asList(type.getDeclaredFields()));
				type = type.getSuperclass();
			}
			return fields;
		}

		private static Set<Field> onlyInjectableFields(Set<Field> fields) {
			return filter(fields, new Predicate<Field>() {
				public boolean apply(Field input) {
					return input.isAnnotationPresent(Inject.class);
				}
			});
		}

		private static Set<Field> onlyLoggerFields(Set<Field> fields) {
			return filter(fields, new Predicate<Field>() {
				public boolean apply(Field input) {
					return input.getType().isAssignableFrom(Log.class);
				}
			});
		}
	}
}