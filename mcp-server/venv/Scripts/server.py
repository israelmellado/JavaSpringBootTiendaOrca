from mcp.server.fastmcp import FastMCP

import mysql.connector

mcp = FastMCP("tienda-orca")

DB = {
    "host": "localhost",
    "user": "root",
    "password": "12345678",
    "database": "tienda_virtual",
    "port": 3306
}

def conn():
    return mysql.connector.connect(**DB)

@mcp.tool()
def verificar_stock(id_articulo: int):
    c = conn()
    cur = c.cursor(dictionary=True)
    cur.execute(
        "SELECT descripcion, stock FROM articulos WHERE id_articulo=%s",
        (id_articulo,)
    )
    r = cur.fetchone()
    c.close()

    if not r:
        return "NO EXISTE"

    return f"{r['descripcion']} stock={r['stock']}"

@mcp.tool()
def actualizar_estado_pedido(id_pedido: int, estado: str):
    c = conn()
    cur = c.cursor()
    cur.execute(
        "UPDATE cabecera_pedidos SET estado=%s WHERE id_pedido=%s",
        (estado, id_pedido)
    )
    c.commit()
    c.close()

    return "OK"


@mcp.tool()
def ping():
    return "pong"

if __name__ == "__main__":
    print("SERVER ARRANCADO", flush=True)
    mcp.run()