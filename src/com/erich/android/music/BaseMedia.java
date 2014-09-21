package com.erich.android.music;

import com.hnapay.android.util.StringUtil;

abstract class BaseMedia
{
	static final int INVALID_ID = -1;

	private final long mId;
	private final String mName;

	BaseMedia( long id, String name )
	{
		this.mId = ( id > INVALID_ID ) ? id : INVALID_ID;
		this.mName = ( name != null ) ? name : StringUtil.EMPTY_STRING;
	}

	public long getId( )
	{
		return this.mId;
	}

	public boolean hasValidId( )
	{
		return ( this.mId > INVALID_ID );
	}

	public String getName( )
	{
		return this.mName;
	}

	@Override
	public String toString( )
	{
		return "Media [ID=" + mId + ", Name=" + mName + "]";
	}

	@Override
	public int hashCode( )
	{
		int result = Long.valueOf( this.mId ).hashCode( );
		return ( ( result << 5 ) - result ) + this.mName.hashCode( );
	}

	@Override
	public boolean equals( Object obj )
	{
		if ( this == obj )
		{
			return true;
		}

		if ( obj instanceof BaseMedia )
		{
			BaseMedia other = ( BaseMedia ) obj;
			return ( this.mId == other.mId ) && StringUtil.equals( this.mName, other.mName );
		}
		else
		{
			return false;
		}
	}
}