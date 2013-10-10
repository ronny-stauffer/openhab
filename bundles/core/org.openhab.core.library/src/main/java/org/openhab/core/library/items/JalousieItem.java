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
package org.openhab.core.library.items;

import java.util.ArrayList;
import java.util.List;

import org.openhab.core.items.GenericItem;
import org.openhab.core.library.types.ExtendedJalousieType;
import org.openhab.core.library.types.JalousieType;
import org.openhab.core.library.types.PercentType;
import org.openhab.core.library.types.StopMoveType;
import org.openhab.core.library.types.UndefinableType;
import org.openhab.core.library.types.UpDownType;
import org.openhab.core.types.Command;
import org.openhab.core.types.State;
import org.openhab.core.types.UnDefType;

/**
 * A JalousieItem allows the control of jalousies, i.e. 
 * moving them up, down, stopping or setting it to close to a certain percentage
 * and opening or closing the slats.
 *  
 * @author Kai Kreuzer
 * @author Ronny Stauffer
 *
 */
public class JalousieItem extends GenericItem {
	
	private static List<Class<? extends State>> acceptedDataTypes = new ArrayList<Class<? extends State>>();
	private static List<Class<? extends Command>> acceptedCommandTypes = new ArrayList<Class<? extends Command>>();
	
	static {
		acceptedDataTypes.add(UnDefType.class);
		acceptedDataTypes.add(UpDownType.class);
		acceptedDataTypes.add(PercentType.class);
		//acceptedDataTypes.add(JalousieType.class); // <-- State type which is internally used within this item
		acceptedDataTypes.add(ExtendedJalousieType.class); // <-- State type which is internally used within this item

		//acceptedCommandTypes.add(JalousieType.class);
		acceptedCommandTypes.add(ExtendedJalousieType.class);
		acceptedCommandTypes.add(PercentType.class);		
		acceptedCommandTypes.add(UpDownType.class);
		acceptedCommandTypes.add(StopMoveType.class);
	}
	
	public JalousieItem(String name) {
		super(name);
	}

	public List<Class<? extends State>> getAcceptedDataTypes() {
		return acceptedDataTypes;
	}

	public List<Class<? extends Command>> getAcceptedCommandTypes() {
		return acceptedCommandTypes;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setState(State state) {
		// Convert state value to internally used state type of this item
		// Map UP/DOWN values to the jalousie values UP and DOWN_AND_SLATS_CLOSED respectively
		if (state == UpDownType.UP) {
			super.setState(ExtendedJalousieType.UP);
		} else if(state == UpDownType.DOWN) {
			super.setState(ExtendedJalousieType.DOWN_AND_SLATS_CLOSED);
		} else if (state.getClass().equals(PercentType.class)) {
			//super.setState(JalousieType.valueOf((PercentType)state, PercentType.ZERO)); // With normal jalousie type
			super.setState(ExtendedJalousieType.valueOf(UndefinableType.valueOf((PercentType)state), UndefinableType.valueOf(PercentType.ZERO))); // With extended jalousie type
		// Only if the type of the given state value corresponds to the internally used state type of this item, no conversion is necessary
		} else if (state.getClass().equals(ExtendedJalousieType.class)) {
			super.setState(state);
		}
		
		// Otherwise, if the state value doesn't correspond to one of the accepted data types list, do nothing
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public State getStateAs(Class<? extends State> typeClass) {
		// Only if the requested type doesn't correspond to the internally used state type (or a super type of it) of this item, a conversion is necessary
		// Convert internal state to requested type
		if (typeClass == UpDownType.class) {
			if (PercentType.ZERO.equals(state)) {
				return UpDownType.UP;
			} else if(PercentType.HUNDRED.equals(state)) {
				return UpDownType.DOWN;
			} else {
				return UnDefType.UNDEF;
			}
		}
		
		// Otherwise
		return super.getStateAs(typeClass);
	}
}
