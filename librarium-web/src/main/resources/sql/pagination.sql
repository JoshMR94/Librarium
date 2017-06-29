/* Usar en session de solicitudes para paginación desde servidor  */

/*
    @NumeroPagina  -- número de página
    @TamañoPagina  -- cantidad de elementos por página
*/

SELECT id, estado, gestion, motivo_solicitud
FROM (
	SELECT id, estado, gestion, motivo_solicitud,
        Row_Number() OVER (ORDER BY id) as RowNum
        FROM solicitud
)S
WHERE 
S.RowNum BETWEEN ((@NumeroPagina-1)*@TamañoPagina)+1 AND (@NumeroPagina*TamañoPagina);