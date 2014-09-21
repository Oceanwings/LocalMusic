package com.erich.android.music;

import java.util.ArrayList;
import java.util.Collection;
import android.support.v4.util.LongSparseArray;

final class MediaTable< V extends BaseMedia >
{
	private static final int INVALID_KEY_VALUE = BaseMedia.INVALID_ID;

	private static final int DEFAULT_INIT_SIZE = 100;

	private LongSparseArray< V > mTable;
	private ArrayList< Long > mIdList;

	MediaTable( )
	{
		this.mTable = new LongSparseArray< V >( DEFAULT_INIT_SIZE );
		this.mIdList = new ArrayList< Long >( DEFAULT_INIT_SIZE );
	}

	MediaTable( final int initialCapacity )
	{
		int size = ( initialCapacity > 0 ) ? initialCapacity : DEFAULT_INIT_SIZE;
		this.mTable = new LongSparseArray< V >( size );
		this.mIdList = new ArrayList< Long >( size );
	}

	void addAll( Collection< V > list )
	{
		if ( list == null || list.isEmpty( ) )
		{
			return;
		}

		synchronized ( this )
		{
			for ( V media : list )
			{
				this.put( media.getId( ), media );
			}
		}
	}

	void put( final long key, V media )
	{
		if ( key <= INVALID_KEY_VALUE )
		{
			return;
		}

		synchronized ( this )
		{
			if ( media != null )
			{
				this.mTable.put( key, media );
				long keyObj = Long.valueOf( media.getId( ) );
				if ( !this.mIdList.contains( keyObj ) )
				{
					this.mIdList.add( keyObj );
				}
			}
			else
			{
				this.mTable.delete( key );
			}
		}
	}

	synchronized V getById( final long id )
	{
		return ( id > INVALID_KEY_VALUE ) ? this.mTable.get( id ) : null;
	}

	V getDataAt( final int index )
	{
		return ( index >= 0 && index < this.size( ) ) ? this.getById( this.mIdList.get( index ) )
				: null;
	}

	void delete( final long key )
	{
		if ( key <= INVALID_KEY_VALUE )
		{
			return;
		}

		synchronized ( this )
		{
			this.mTable.delete( key );
			this.mIdList.remove( Long.valueOf( key ) );
		}
	}

	long[ ] getIdArray( )
	{
		long[ ] ids = new long[ this.mIdList.size( ) ];
		int index = 0;
		for ( Long idObj : this.mIdList )
		{
			ids[ index++ ] = idObj.longValue( );
		}
		return ids;
	}

	synchronized int size( )
	{
		return this.mIdList.size( );
	}

	synchronized void clear( )
	{
		this.mTable.clear( );
		this.mIdList.clear( );
	}
}