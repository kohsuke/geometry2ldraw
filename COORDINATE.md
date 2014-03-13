The header of the ASC file looks like this:

    ncols        840
    nrows        603
    xllcorner    7.553101000000
    yllcorner    45.904949789286
    cellsize     0.000277927381
    NODATA_value -32768

The format is called [ASCII ARC file](http://en.wikipedia.org/wiki/Esri_grid)

cellsize very closely matches 1/3600, so I assume it means 1 arc-second,
[the resolution of ASTER GDEM data](https://lpdaac.usgs.gov/products/aster_products_table/astgtm)
so the pixel size is 1 arc second, that is 2*pi*r / (360*60*60) = 30.92m

The data value is in meters.

The *.prj file looks like this:

    GEOGCS["GCS_WGS_1984",
      DATUM["D_WGS_1984",
        SPHEROID["WGS_1984",6378137,298.257223563]
      ],
      PRIMEM["Greenwich",0],
      UNIT["Degree",0.017453292519943295]
    ]

The format is explained [here](http://www.geoapi.org/3.0/javadoc/org/opengis/referencing/doc-files/WKT.html)