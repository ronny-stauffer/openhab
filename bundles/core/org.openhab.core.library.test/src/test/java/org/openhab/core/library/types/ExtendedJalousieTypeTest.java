package org.openhab.core.library.types;

import static org.junit.Assert.*;

import org.junit.Test;
import org.omg.CosNaming.IstringHelper;

public class ExtendedJalousieTypeTest {

	@Test
	public void testValueOfNativeDefined() {
		ExtendedJalousieType jalousieValue = ExtendedJalousieType.valueOf(
				UndefinableType.valueOf(new PercentType(11)),
				UndefinableType.valueOf(new PercentType(22)));

		assertTrue(jalousieValue.isSet());
		assertEquals(11, jalousieValue.getValueSet().intValue());
		assertTrue(jalousieValue.getSlatsOpeningValue().isSet());
		assertEquals(22, jalousieValue.getSlatsOpeningValue().getValueSet()
				.intValue());
		assertFalse(jalousieValue.isChanging());
	}

	@Test
	public void testValueOfNativeUndefinedValue() {
		ExtendedJalousieType jalousieValue = ExtendedJalousieType.valueOf(
				UndefinableType.<PercentType> UNDEFINED(),
				UndefinableType.valueOf(new PercentType(22)));

		assertFalse(jalousieValue.isSet());
		assertTrue(jalousieValue.getSlatsOpeningValue().isSet());
		assertEquals(22, jalousieValue.getSlatsOpeningValue().getValueSet()
				.intValue());
		assertFalse(jalousieValue.isChanging());
	}

	@Test
	public void testValueOfNativeUndefinedSlatsOpeningValue() {
		ExtendedJalousieType jalousieValue = ExtendedJalousieType.valueOf(
				UndefinableType.valueOf(new PercentType(11)),
				UndefinableType.<PercentType> UNDEFINED());

		assertTrue(jalousieValue.isSet());
		assertEquals(11, jalousieValue.getValueSet().intValue());
		assertFalse(jalousieValue.getSlatsOpeningValue().isSet());
		assertFalse(jalousieValue.isChanging());
	}

	@Test
	public void testValueOfNativeUndefined() {
		ExtendedJalousieType jalousieValue = ExtendedJalousieType.valueOf(
				UndefinableType.<PercentType> UNDEFINED(),
				UndefinableType.<PercentType> UNDEFINED());

		assertFalse(jalousieValue.isSet());
		assertFalse(jalousieValue.getSlatsOpeningValue().isSet());
		assertFalse(jalousieValue.isChanging());
	}

	@Test
	public void testValueOfNativeIsChanging() {
		ExtendedJalousieType jalousieValue = ExtendedJalousieType.valueOf(
				UndefinableType.valueOf(new PercentType(88)),
				UndefinableType.valueOf(new PercentType(99)),
				ExtendedJalousieType.Flags.IS_CHANGING);

		assertTrue(jalousieValue.isSet());
		assertEquals(88, jalousieValue.getValueSet().intValue());
		assertTrue(jalousieValue.getSlatsOpeningValue().isSet());
		assertEquals(99, jalousieValue.getSlatsOpeningValue().getValueSet()
				.intValue());
		assertTrue(jalousieValue.isChanging());
	}

	@Test
	public void testValueOfStringDefined() {
		ExtendedJalousieType jalousieValue = ExtendedJalousieType
				.valueOf("11:22");

		assertTrue(jalousieValue.isSet());
		assertEquals(11, jalousieValue.getValueSet().intValue());
		assertTrue(jalousieValue.getSlatsOpeningValue().isSet());
		assertEquals(22, jalousieValue.getSlatsOpeningValue().getValueSet()
				.intValue());
		assertFalse(jalousieValue.isChanging());
	}

	@Test
	public void testValueOfStringUndefinedValue() {
		ExtendedJalousieType jalousieValue = ExtendedJalousieType
				.valueOf("-:22");

		assertFalse(jalousieValue.isSet());
		assertTrue(jalousieValue.getSlatsOpeningValue().isSet());
		assertEquals(22, jalousieValue.getSlatsOpeningValue().getValueSet()
				.intValue());
		assertFalse(jalousieValue.isChanging());
	}

	@Test
	public void testValueOfStringUndefinedSlatsOpeningValue() {
		ExtendedJalousieType jalousieValue = ExtendedJalousieType
				.valueOf("11:-");

		assertTrue(jalousieValue.isSet());
		assertEquals(11, jalousieValue.getValueSet().intValue());
		assertFalse(jalousieValue.getSlatsOpeningValue().isSet());
		assertFalse(jalousieValue.isChanging());
	}

	@Test
	public void testValueOfStringUndefined() {
		ExtendedJalousieType jalousieValue = ExtendedJalousieType
				.valueOf("-:-");

		assertFalse(jalousieValue.isSet());
		assertFalse(jalousieValue.getSlatsOpeningValue().isSet());
		assertFalse(jalousieValue.isChanging());
	}

	@Test
	public void testValueOfStringIsChanging() {
		ExtendedJalousieType jalousieValue = ExtendedJalousieType
				.valueOf("+88:99");

		assertTrue(jalousieValue.isSet());
		assertEquals(88, jalousieValue.getValueSet().intValue());
		assertTrue(jalousieValue.getSlatsOpeningValue().isSet());
		assertEquals(99, jalousieValue.getSlatsOpeningValue().getValueSet()
				.intValue());
		assertTrue(jalousieValue.isChanging());
	}

	@Test
	public void testValueOfStringUndefinedIsChanging() {
		ExtendedJalousieType jalousieValue = ExtendedJalousieType.valueOf("+-");

		assertFalse(jalousieValue.isSet());
		assertFalse(jalousieValue.getSlatsOpeningValue().isSet());
		assertTrue(jalousieValue.isChanging());
	}

	@Test
	public void testValueOfStringUndefinedIsChanging2() {
		ExtendedJalousieType jalousieValue = ExtendedJalousieType
				.valueOf("+-:-");

		assertFalse(jalousieValue.isSet());
		assertFalse(jalousieValue.getSlatsOpeningValue().isSet());
		assertTrue(jalousieValue.isChanging());
	}

	@Test
	public void testEquals() {
		ExtendedJalousieType jalousieValue = ExtendedJalousieType
				.valueOf("11:22");
		ExtendedJalousieType otherJalousieValue = ExtendedJalousieType
				.valueOf("11:22");

		assertTrue(jalousieValue.equals(otherJalousieValue));
		assertTrue(otherJalousieValue.equals(jalousieValue));
	}

	@Test
	public void testEqualsNoEquality() {
		ExtendedJalousieType jalousieValue = ExtendedJalousieType
				.valueOf("11:22");
		ExtendedJalousieType otherJalousieValue = ExtendedJalousieType
				.valueOf("22:33");

		assertFalse(jalousieValue.equals(otherJalousieValue));
		assertFalse(otherJalousieValue.equals(jalousieValue));
	}

	@Test
	public void testUndefinedEquals() {
		ExtendedJalousieType jalousieValue = ExtendedJalousieType
				.valueOf("-:22");
		ExtendedJalousieType otherJalousieValue = ExtendedJalousieType
				.valueOf("-:22");

		assertTrue(jalousieValue.equals(otherJalousieValue));
		assertTrue(otherJalousieValue.equals(jalousieValue));
	}

	@Test
	public void testUndefinedEqualsNoEquality() {
		ExtendedJalousieType jalousieValue = ExtendedJalousieType
				.valueOf("-:22");
		ExtendedJalousieType otherJalousieValue = ExtendedJalousieType
				.valueOf("-:33");

		assertFalse(jalousieValue.equals(otherJalousieValue));
		assertFalse(otherJalousieValue.equals(jalousieValue));
	}

	@Test
	public void testUndefinedEquals2() {
		ExtendedJalousieType jalousieValue = ExtendedJalousieType
				.valueOf("11:-");
		ExtendedJalousieType otherJalousieValue = ExtendedJalousieType
				.valueOf("11:-");

		assertTrue(jalousieValue.equals(otherJalousieValue));
		assertTrue(otherJalousieValue.equals(jalousieValue));
	}

	@Test
	public void testUndefinedEqualsNoEquality2() {
		ExtendedJalousieType jalousieValue = ExtendedJalousieType
				.valueOf("11:-");
		ExtendedJalousieType otherJalousieValue = ExtendedJalousieType
				.valueOf("22:-");

		assertFalse(jalousieValue.equals(otherJalousieValue));
		assertFalse(otherJalousieValue.equals(jalousieValue));
	}

	@Test
	public void testUndefinedEquals3() {
		ExtendedJalousieType jalousieValue = ExtendedJalousieType.valueOf("-");
		ExtendedJalousieType otherJalousieValue = ExtendedJalousieType
				.valueOf("-");

		assertTrue(jalousieValue.equals(otherJalousieValue));
		assertTrue(otherJalousieValue.equals(jalousieValue));
	}

	@Test
	public void testUndefinedEqualsNoEquality3() {
		ExtendedJalousieType jalousieValue = ExtendedJalousieType
				.valueOf("11:22");
		ExtendedJalousieType otherJalousieValue = ExtendedJalousieType
				.valueOf("-");

		assertFalse(jalousieValue.equals(otherJalousieValue));
		assertFalse(otherJalousieValue.equals(jalousieValue));
	}

	@Test
	public void testMatchesWildcard() {
		ExtendedJalousieType jalousieValue = ExtendedJalousieType
				.valueOf("11:22");
		ExtendedJalousieType otherJalousieValue = ExtendedJalousieType
				.valueOf("*:22");

		assertTrue(jalousieValue.matches(otherJalousieValue));
		assertTrue(otherJalousieValue.matches(jalousieValue));
	}

	@Test
	public void testMatchesWildcardNoMatch() {
		ExtendedJalousieType jalousieValue = ExtendedJalousieType
				.valueOf("11:22");
		ExtendedJalousieType otherJalousieValue = ExtendedJalousieType
				.valueOf("*:33");

		assertFalse(jalousieValue.matches(otherJalousieValue));
		assertFalse(otherJalousieValue.matches(jalousieValue));
	}

	@Test
	public void testMatchesWildcard2() {
		ExtendedJalousieType jalousieValue = ExtendedJalousieType
				.valueOf("11:22");
		ExtendedJalousieType otherJalousieValue = ExtendedJalousieType
				.valueOf("11:*");

		assertTrue(jalousieValue.matches(otherJalousieValue));
		assertTrue(otherJalousieValue.matches(jalousieValue));
	}

	@Test
	public void testMatchesWildcardNoMatch2() {
		ExtendedJalousieType jalousieValue = ExtendedJalousieType
				.valueOf("11:22");
		ExtendedJalousieType otherJalousieValue = ExtendedJalousieType
				.valueOf("22:*");

		assertFalse(jalousieValue.matches(otherJalousieValue));
		assertFalse(otherJalousieValue.matches(jalousieValue));
	}

	@Test
	public void testMatchesWildcard3() {
		ExtendedJalousieType jalousieValue = ExtendedJalousieType
				.valueOf("11:22");
		ExtendedJalousieType otherJalousieValue = ExtendedJalousieType
				.valueOf("*");

		assertTrue(jalousieValue.matches(otherJalousieValue));
		assertTrue(otherJalousieValue.matches(jalousieValue));
	}

	@Test
	public void testUndefinedMatchesWildcard() {
		ExtendedJalousieType jalousieValue = ExtendedJalousieType
				.valueOf("-:22");
		ExtendedJalousieType otherJalousieValue = ExtendedJalousieType
				.valueOf("*:22");

		assertTrue(jalousieValue.matches(otherJalousieValue));
		assertTrue(otherJalousieValue.matches(jalousieValue));
	}

	@Test
	public void testUndefinedMatchesWildcard2() {
		ExtendedJalousieType jalousieValue = ExtendedJalousieType
				.valueOf("11:-");
		ExtendedJalousieType otherJalousieValue = ExtendedJalousieType
				.valueOf("11:*");

		assertTrue(jalousieValue.matches(otherJalousieValue));
		assertTrue(otherJalousieValue.matches(jalousieValue));
	}

	@Test
	public void testUndefinedMatchesWildcard3() {
		ExtendedJalousieType jalousieValue = ExtendedJalousieType.valueOf("-");
		ExtendedJalousieType otherJalousieValue = ExtendedJalousieType
				.valueOf("*");

		assertTrue(jalousieValue.matches(otherJalousieValue));
		assertTrue(otherJalousieValue.matches(jalousieValue));
	}

	@Test
	public void testMatchesDoubleWildcard() {
		ExtendedJalousieType jalousieValue = ExtendedJalousieType.valueOf("*");
		ExtendedJalousieType otherJalousieValue = ExtendedJalousieType
				.valueOf("*");

		assertTrue(jalousieValue.matches(otherJalousieValue));
		assertTrue(otherJalousieValue.matches(jalousieValue));
	}

	@Test
	public void testMatchesWildcardIsChanging() {
		ExtendedJalousieType jalousieValue = ExtendedJalousieType
				.valueOf("+11:22");
		ExtendedJalousieType otherJalousieValue = ExtendedJalousieType
				.valueOf("+*");

		assertTrue(jalousieValue.matches(otherJalousieValue));
		assertTrue(otherJalousieValue.matches(jalousieValue));
	}

	@Test
	public void testMatchesWildcardIsChangingNoMatch() {
		ExtendedJalousieType jalousieValue = ExtendedJalousieType
				.valueOf("+11:22");
		ExtendedJalousieType otherJalousieValue = ExtendedJalousieType
				.valueOf("*");

		assertFalse(jalousieValue.matches(otherJalousieValue));
		assertFalse(otherJalousieValue.matches(jalousieValue));
	}

	@Test
	public void testMatchesWildcardIsChangingNoMatch2() {
		ExtendedJalousieType jalousieValue = ExtendedJalousieType
				.valueOf("11:22");
		ExtendedJalousieType otherJalousieValue = ExtendedJalousieType
				.valueOf("+*");

		assertFalse(jalousieValue.matches(otherJalousieValue));
		assertFalse(otherJalousieValue.matches(jalousieValue));
	}

	@Test
	public void testValueOfClosedLiteral() {
		ExtendedJalousieType jalousieValue = ExtendedJalousieType
				.valueOf("CLOSED");

		assertTrue(jalousieValue.isSet());
		assertEquals(100, jalousieValue.getValueSet().intValue());
		assertTrue(jalousieValue.getSlatsOpeningValue().isSet());
		assertEquals(0, jalousieValue.getSlatsOpeningValue().getValueSet()
				.intValue());
		assertFalse(jalousieValue.isChanging());
	}

	@Test
	public void testValueOfClosedLiteral2() {
		ExtendedJalousieType jalousieValue = ExtendedJalousieType
				.valueOf("CLOSED");
		ExtendedJalousieType closedJalousieValue = ExtendedJalousieType
				.valueOf("100:0");

		assertEquals(closedJalousieValue, jalousieValue);
	}

	public void testValueOfNotClosedLiteral() {
		ExtendedJalousieType jalousieValue = ExtendedJalousieType
				.valueOf("NOT_CLOSED");
		ExtendedJalousieType closedJalousieValue = ExtendedJalousieType
				.valueOf("100:0");

		assertFalse(closedJalousieValue.equals(jalousieValue));
	}
}
