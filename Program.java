import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.LinkedList;

class Program
{
	class Lex
	{
		public String value;
		public Type type;
	};

	private static boolean checkForOperator(String token, Scanner line, Scanner file, LinkedList<Lex> list)
	{
		if(token.equals("++") || token.equals("--") || token.equals("+") ||
			token.equals("-") || token.equals("*") || token.equals("/") ||
			token.equals("%") || token.equals("=") || token.equals("+=") ||
			token.equals("-=") || token.equals("/=") || token.equals("*=") ||
			token.equals("%=") || token.equals("==") || token.equals("<") ||
			token.equals(">") || token.equals("!=") || token.equals("&&") ||
			token.equals("||") || token.equals("!") || token.equals("<=") ||
			token.equals(">=") || token.equals("&") || token.equals("|") ||
			token.equals("^") || token.equals("~") || token.equals("->") ||
			token.equals(">>") || token.equals("<<"))
		{
			Lex lex = new Lex(token, Type.OPERATOR);
			list.add(lex);
			return true;
		}
	}

	public static void main(String[] args)
	{
		if (args.length != 1)
		{
			System.out.println("Usage: java Program <file>");
		}
		else
		{
			try
			{
				File file = new File(args[0]);
				Scanner sc = new Scanner(file).useDelimiter("\n");
				LinkedList<Lex>	list = new LinkedList<Lex>();

				while(sc.hasNext())
				{
					Scanner t = new Scanner(sc.next()).useDelimiter("\s");

					while(t.hasNext())
					{
						String cur = t.next();

						identifyToken(cur, t, sc, list);
					}
				}
			}
		}
	}
}