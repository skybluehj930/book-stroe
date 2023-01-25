package com.lhj.bookstore.entity;

import java.io.Serializable;

import lombok.Builder;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class SupplyBookId implements Serializable {
	
	private Long supply;
	
	private Long bookInfo;
	
	@Builder
	public SupplyBookId(Long supply, Long bookInfo) {
		this.supply = supply;
		this.bookInfo = bookInfo;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((bookInfo == null) ? 0 : bookInfo.hashCode());
		result = prime * result + ((supply == null) ? 0 : supply.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SupplyBookId other = (SupplyBookId) obj;
		if (bookInfo == null) {
			if (other.bookInfo != null)
				return false;
		} else if (!bookInfo.equals(other.bookInfo))
			return false;
		if (supply == null) {
			if (other.supply != null)
				return false;
		} else if (!supply.equals(other.supply))
			return false;
		return true;
	}
	
}
