/**
 * openHAB, the open Home Automation Bus.
 * Copyright (C) 2010-2013, openHAB.org <admin@openhab.org>
 *
 * See the contributors.txt file in the distribution for a
 * full listing of individual contributors.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation; either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, see <http://www.gnu.org/licenses>.
 *
 * Additional permission under GNU GPL version 3 section 7
 *
 * If you modify this Program, or any covered work, by linking or
 * combining it with Eclipse (or a modified version of that library),
 * containing parts covered by the terms of the Eclipse Public License
 * (EPL), the licensors of this Program grant you additional permission
 * to convey the resulting work.
 */
package org.openhab.core.library.types;

import java.util.Arrays;
import java.util.Collections;
import java.util.EnumSet;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import org.openhab.core.types.Command;
import org.openhab.core.types.ComplexType;
import org.openhab.core.types.PrimitiveType;
import org.openhab.core.types.State;

/**
 * The JalousieType is a complex type with constituents for a percent value and
 * and a slats opening value and can be used for jalousie items.
 * 
 * @author Ronny Stauffer
 * @since ...
 * 
 */
public class ExtendedJalousieType extends UndefinableType<PercentType> implements TransitionallyChangingType, ComplexType, State, Command {

	private static final long serialVersionUID = 322902950356613226L;

	public enum Flags {
		NONE,
		IS_CHANGING
	}
	
	private static final String EMPTY_STRING = "";
	private static final String IS_CHANGING_TOKEN = "+";
	
	public static final String UP_LITERAL = "UP";
	public static final String CLOSED_LITERAL = "CLOSED";
	public static final String NOT_CLOSED_LITERAL = "NOT_CLOSED";
	public static final String UNDEFINED_LITERAL = "UNDEFINED";
	
	// Constants for the constituents
	static final public String KEY_VALUE = "v";
	static final public String KEY_SLATS_OPENING_VALUE = "s";

	// Constants for boundary values
	static final public ExtendedJalousieType UNDEFINED = new ExtendedJalousieType(UndefinableType.<PercentType>UNDEFINED(), UndefinableType.<PercentType>UNDEFINED());
	static final public ExtendedJalousieType UP = new ExtendedJalousieType(UndefinableType.valueOf(PercentType.ZERO), UndefinableType.<PercentType>UNDEFINED());
	static final public ExtendedJalousieType DOWN_AND_SLATS_CLOSED = new ExtendedJalousieType(UndefinableType.valueOf(PercentType.HUNDRED), UndefinableType.valueOf(PercentType.ZERO));
	static final public ExtendedJalousieType CLOSED = DOWN_AND_SLATS_CLOSED; // Alias
	

	// The inherited field "value" of the parent DecimalType corresponds to the
	// "value" constituent of this complex type.
	
	protected UndefinableType<PercentType> slatsOpeningValue;
	
	//TODO Implement this on UndefinableType?
	protected boolean isTransitionallyChanging;

	// The constructor is not part of the public API.
	private ExtendedJalousieType(UndefinableType<PercentType> value, UndefinableType<PercentType> slatsOpeningValue, Flags... _flags) {
		super(value.value, value.isWildcard);
		this.slatsOpeningValue = slatsOpeningValue;
		
		Set<Flags> flags = EnumSet.<Flags>of(Flags.NONE, _flags);
		if (flags.contains(Flags.IS_CHANGING)) {
			this.isTransitionallyChanging = true;
		}
	}

	// Instead, here are static factory methods to be used for instance creation.
	public static ExtendedJalousieType valueOf(UndefinableType<PercentType> value, UndefinableType<PercentType> slatsOpeningValue, Flags... flags) {
		if (value == null) {
			throw new IllegalArgumentException("value must not be null!");
		}
		if (slatsOpeningValue == null) {
			throw new IllegalArgumentException("slatsOpeningValue must not be null!");
		}
		return new ExtendedJalousieType(value, slatsOpeningValue, flags);
	}
	// This static factory method is used by the openHAB scripting engine
	// in order to create an instance of this type when a literal is specified in a script.
	public static ExtendedJalousieType valueOf(final String literalValue) {
		if (literalValue == null) {
			throw new /* NullPointerException */ IllegalArgumentException(
				"literalValue must not be null!");
		}
		
		String buffer = literalValue;
		
		if (UP_LITERAL.equals(buffer)) {
			return UP;
		} else if (CLOSED_LITERAL.equals(buffer)) {
			return DOWN_AND_SLATS_CLOSED;
		} else if (NOT_CLOSED_LITERAL.equals(buffer)) {
			//TODO
			return UNDEFINED;
		} else if (UNDEFINED_LITERAL.equals(buffer)) {
			return UNDEFINED;
		}
		
		UndefinableType<PercentType> value;
		UndefinableType<PercentType> slatsOpeningValue;
		Set<Flags> flags = EnumSet.noneOf(Flags.class);
		
		// Remove all possibly surrounding (single and double) quotes.
		// Quotes are present if the state is specified as 'STRING' token (and not as 'ID' or 'Number')
		// See also: RuleTriggerManager.internalGetRules()
		buffer = buffer.replaceAll("^\'|^\"|\'$|\"$", "");
		
		if (buffer.startsWith(IS_CHANGING_TOKEN) && buffer.length() > IS_CHANGING_TOKEN.length()) {
			flags.add(Flags.IS_CHANGING);
			
			buffer = buffer.substring(IS_CHANGING_TOKEN.length());
		}
		
		if (UndefinableType.UNDEFINED_LITERAL.equals(buffer)) {
			value = UndefinableType.<PercentType>UNDEFINED();
			slatsOpeningValue = UndefinableType.<PercentType>UNDEFINED();
		} else if (UndefinableType.WILDCARD_LITERAL.equals(buffer)) {
			value = (UndefinableType<PercentType>)UndefinableType.WILDCARD;
			slatsOpeningValue = (UndefinableType<PercentType>)UndefinableType.WILDCARD;
		} else {
			final String[] constituents = buffer.split(":");
			if (constituents.length == 2) {
				value = UndefinableType.valueOf(constituents[0], new ValueCreator<PercentType>() {
					@Override
					public PercentType create(String value) {
						return new PercentType(constituents[0]); // Cast to PercentType includes syntax check!
					}
				});
				slatsOpeningValue = UndefinableType.valueOf(constituents[1], new ValueCreator<PercentType>() {
					@Override
					public PercentType create(String value) {
						return new PercentType(constituents[1]);
					}
				});
			} else {
				throw new IllegalArgumentException(String.format("'%s' is not a valid jalousie type literal!", literalValue));
			}
		}
		
		return new ExtendedJalousieType(value, slatsOpeningValue, flags.toArray(new Flags[] {}));
	}

	// Getter for constituents map
	@Override
	public SortedMap<String, PrimitiveType> getConstituents() {
		TreeMap<String, PrimitiveType> constituents = new TreeMap<String, PrimitiveType>();
		constituents.put(KEY_VALUE, UndefinableType.valueOf(value));
		constituents.put(KEY_SLATS_OPENING_VALUE, slatsOpeningValue);
		return Collections.unmodifiableSortedMap(constituents);
	}

	// Getters for the constituents
	public UndefinableType<PercentType> getValue() {
		return UndefinableType.valueOf(value);
	}	
	public UndefinableType<PercentType> getSlatsOpeningValue() {
		return slatsOpeningValue;
	}
	
	public boolean isTransitionallyChanging() {
		return isTransitionallyChanging;
	}

	@Override
	public boolean equals(Object other) {
		if (other == null) {
			return false;
		}
		if (other == this) {
			return true;
		}
		if (!(other instanceof ExtendedJalousieType)) {
			return false;
		}
		ExtendedJalousieType otherJalousieType = (ExtendedJalousieType)other;
		if (!(super.equals(otherJalousieType)
				&& slatsOpeningValue.equals(otherJalousieType.slatsOpeningValue)
				&& isTransitionallyChanging == otherJalousieType.isTransitionallyChanging)) {
			return false;
		}
		
		return super.equals(other);
	}
	
	@Override
	public boolean matches(Object other) {
		if (other == null) {
			return false;
		}
		if (this == other) {
			return true;
		}
		if (!(other instanceof ExtendedJalousieType)) {
			return false;
		}
		ExtendedJalousieType otherJalousieType = (ExtendedJalousieType)other;
		UndefinableType<PercentType> _value = new UndefinableType<PercentType>(value, isWildcard);
		if (!(/* isWildcard
				|| otherJalousieType.isWildcard
				|| */ _value.matches(otherJalousieType)
						&& slatsOpeningValue.matches(otherJalousieType.slatsOpeningValue)
						&& isTransitionallyChanging == otherJalousieType.isTransitionallyChanging)) {
			return false;
		}
		
		return _value.matches(other);
	}
	
	@Override
	public int hashCode() {
		int hashCode = 100 * slatsOpeningValue.hashCode();
		hashCode += super.hashCode();
		
		return hashCode;
	}
	
	public String toString() {
		return String.format("%s%s:%s", isTransitionallyChanging ? IS_CHANGING_TOKEN : EMPTY_STRING, super.toString(), slatsOpeningValue);
	}
}
