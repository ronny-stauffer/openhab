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

import java.util.Collections;
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
public class ExtendedJalousieType extends UndefinableType<PercentType> implements ComplexType, State, Command {

	private static final long serialVersionUID = 322902950356613226L;

	// Constants for the constituents
	static final public String KEY_VALUE = "v";
	static final public String KEY_SLATS_OPENING_VALUE = "s";

	// Constants for boundary values
	static final public ExtendedJalousieType UP = new ExtendedJalousieType(UndefinableType.valueOf(PercentType.ZERO), UndefinableType.<PercentType>UNDEFINED());
	static final public ExtendedJalousieType DOWN_AND_SLATS_CLOSED = new ExtendedJalousieType(UndefinableType.valueOf(PercentType.HUNDRED), UndefinableType.valueOf(PercentType.ZERO));

	// The inherited field "value" of the parent DecimalType corresponds to the
	// "value" constituent of this complex type.
	
	protected UndefinableType<PercentType> slatsOpeningValue;	

	// The constructor is not part of the public API.
	private ExtendedJalousieType(UndefinableType<PercentType> value, UndefinableType<PercentType> slatsOpeningValue) {
		super(value.value);
		this.slatsOpeningValue = slatsOpeningValue;
	}

	// Instead, here are static factory methods to be used for instance creation. 
	public static ExtendedJalousieType valueOf(UndefinableType<PercentType> value, UndefinableType<PercentType> slatsOpeningValue) {
		if (value == null) {
			throw new IllegalArgumentException("value must not be null!");
		}
		if (slatsOpeningValue == null) {
			throw new IllegalArgumentException("slatsOpeningValue must not be null!");
		}
		return new ExtendedJalousieType(value, slatsOpeningValue);
	}
	// This static factory method is used by the openHAB scripting engine
	// in order to create an instance of this type when a literal is specified in a script.
	public static ExtendedJalousieType valueOf(String literalValue) {
		if (literalValue == null) {
			throw new /* NullPointerException */ IllegalArgumentException(
				"literalValue must not be null!");
		}
		
		UndefinableType<PercentType> value;
		UndefinableType<PercentType> slatsOpeningValue;
		final String[] constituents = literalValue.split(":");
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
		
		return new ExtendedJalousieType(value, slatsOpeningValue);
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
		if (super.equals(otherJalousieType) && slatsOpeningValue.equals(otherJalousieType.slatsOpeningValue)) {
			return true;
		}
		
		return super.equals(other);
	}
	
	@Override
	public int hashCode() {
		int hashCode = 100 * slatsOpeningValue.hashCode();
		hashCode += super.hashCode();
		
		return hashCode;
	}
	
	public String toString() {
		return String.format("%s:%s", super.toString(), slatsOpeningValue);
	}
}
