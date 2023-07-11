package dev.jdm.full_slabs.util;

public interface MixinSelf<T> {
	@SuppressWarnings("unchecked")
	default T self() {
		return (T) this;
	}
}
