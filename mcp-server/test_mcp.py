from mcp.server.fastmcp import FastMCP

mcp = FastMCP("test")

@mcp.tool()
def hola():
    return "hola"

if __name__ == "__main__":
    print("ANTES RUN")
    mcp.run()
    print("DESPUES RUN")