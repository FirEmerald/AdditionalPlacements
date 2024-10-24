package com.firemerald.additionalplacements.util.stairs;

import com.firemerald.additionalplacements.util.ComplexFacing;

public enum StairFacingType {
	NORMAL {
		@Override
		public ComplexFacing fromCompressedFacing(CompressedStairFacing facing) {
			return facing.normal;
		}
	},
	FLIPPED {
		@Override
		public ComplexFacing fromCompressedFacing(CompressedStairFacing facing) {
			return facing.flipped;
		}
	},
	VERTICAL {
		@Override
		public ComplexFacing fromCompressedFacing(CompressedStairFacing facing) {
			return facing.vertical;
		}
	};
	
	public abstract ComplexFacing fromCompressedFacing(CompressedStairFacing facing);
}
