package com.erich.android.music;

import java.util.ArrayList;
import com.hnapay.android.util.StringUtil;

abstract class MediaCollection extends BaseMedia
{
	private final String mKey;

	private final ArrayList< Long > mItemIdList = new ArrayList< Long >( 100 );

	MediaCollection( int id, String name, String key )
	{
		super( id, name );

		this.mKey = ( key != null ) ? key : StringUtil.EMPTY_STRING;
	}

	final String getKey( )
	{
		return this.mKey;
	}

	final int getSubItemCount( )
	{
		return this.mItemIdList.size( );
	}

	final long[ ] getItemIds( )
	{
		final long[ ] ids = new long[ this.getSubItemCount( ) ];

		int index = 0;
		for ( Long id : this.mItemIdList )
		{
			ids[ index++ ] = id.longValue( );
		}

		return ids;
	}

	final long getItemIdAt( int location )
	{
		if ( location >= 0 && location < this.getSubItemCount( ) )
		{
			return this.mItemIdList.get( location );
		}
		else
		{
			return INVALID_ID;
		}
	}

	final void addAllSubItemIds( long[ ] ids )
	{
		if ( ids == null || ids.length == 0 )
		{
			return;
		}

		for ( long id : ids )
		{
			this.addSubItemId( id );
		}
	}

	final void addSubItemId( long id )
	{
		if ( id <= INVALID_ID )
		{
			return;
		}

		Long idObject = Long.valueOf( id );
		if ( !this.mItemIdList.contains( idObject ) )
		{
			this.mItemIdList.add( idObject );
		}
	}

	@Override
	public String toString( )
	{
		return "MediaCollection [" + ", ID=" + this.getId( ) + ", Name=" + this.getName( ) + "Key="
				+ this.mKey + ", Sub Item Ids=" + this.mItemIdList + ']';
	}

	@Override
	public int hashCode( )
	{
		int result = super.hashCode( );
		result = ( ( result << 5 ) - result ) + this.mItemIdList.hashCode( );
		result = ( ( result << 5 ) - result ) + this.mKey.hashCode( );
		return result;
	}

	@Override
	public boolean equals( Object obj )
	{
		if ( !super.equals( obj ) || obj instanceof MediaCollection )
		{
			return false;
		}

		MediaCollection other = ( MediaCollection ) obj;
		return StringUtil.equals( this.mKey, other.mKey )
				&& this.mItemIdList.equals( other.mItemIdList );
	}
}