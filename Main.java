import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

class Program
{
	enum Type
	{
		OPERATOR,
		COMMENT,
		IDENTIFIER,
		RESERVED_WORD,
		ERROR,
		PREPROCESSOR,
		STRING,
		CHAR,
		DECIMAL,
		OCTAL,
		BINARY,
		HEXADECIMAL,
		SEPARATOR,
		DOUBLE,
		FLOAT,
		BRACKET
	}
	class Lex
	{
		public String value;
		public Type type;
		Lex(String value, Type type)
		{
			this.value = value;
			this.type = type;
		}
		public String getTypeString()
		{
			switch (this.type)
			{
				case OPERATOR:
					return "operator";
				case COMMENT:
					return "comment";
				case IDENTIFIER:
					return "identifier";
				case RESERVED_WORD:
					return "reserved word";
				case ERROR:
					return "unrecognized";
				case PREPROCESSOR:
					return "preprocessor directive";
				case STRING:
					return "string";
				case CHAR:
					return "char";
				case DECIMAL:
					return "decimal";
				case BINARY:
					return "binary";
				case HEXADECIMAL:
					return "hexadecimal";
				case OCTAL:
					return "octal";
				case SEPARATOR:
					return "separator";
				case DOUBLE:
					return "double";
				case FLOAT:
					return "float";
				case BRACKET:
					return "bracket";
			}
			return "unrecognized";
		}
	};

	private void printLex(Lex lex)
	{
		System.out.printf("%s <%s>  ", lex.value, lex.getTypeString());
	}

	private boolean checkForOperator(String token, Scanner line)
	{
		if(token.equals("+") || token.equals("-") || token.equals("*") ||
			token.equals("/") || token.equals("%") || token.equals("=") ||
			token.equals("-=") || token.equals("/=") || token.equals("*=") ||
			token.equals("%=") || token.equals("==") || token.equals("<") ||
			token.equals(">") || token.equals("!=") || token.equals("&&") ||
			token.equals("||") || token.equals("<=") ||
			token.equals(">=") || token.equals("+="))
		{
			Lex lex = new Lex(token, Type.OPERATOR);
			printLex(lex);
			return true;
		}
		return false;
	}

	private boolean checkForBracket(String token, Scanner line)
	{
		if (token.equals("{") || token.equals("}") ||
			token.equals(")") || token.equals("("))
		{
			Lex lex = new Lex(token, Type.BRACKET);
			printLex(lex);
			return true;
		}
		return false;
	}

	private boolean checkForReserved(String token, Scanner line)
	{
		if (token.equals("int") || token.equals("char") || token.equals("float") ||
			token.equals("double") || token.equals("while") || token.equals("return"))
		{
			Lex lex = new Lex(token, Type.RESERVED_WORD);
			printLex(lex);
			return true;
		}
		return false;
	}

	private boolean checkForChar(String token, Scanner line)
	{
		if ((token.length() == 3 && token.charAt(0) == '\'' && token.charAt(2) == '\'' && token.charAt(1) != '\'' && token.charAt(1) != '\\') ||
			(token.length() == 4 && token.charAt(0) == '\'' && token.charAt(3) == '\'' && token.charAt(1) == '\\'))
		{
			Lex lex = new Lex(token, Type.CHAR);
			printLex(lex);
			return true;
		}
		return false;
	}

	private boolean checkForPreprocessor(String token, Scanner line)
	{
		if (token.equals("#include") || token.equals("#define") || token.equals("#ifdef") ||
			token.equals("#ifndef") || token.equals("#endif"))
		{
			Lex lex = new Lex(token, Type.PREPROCESSOR);
			printLex(lex);
			return true;
		}
		return false;
	}

	private int charToInt(char c)
	{
		if (c >= '0' && c <= '9')
			return (c - '0');
		if (c >= 'a' && c <= 'f')
			return (c - 'a' + 10);
		if (c == 'x')
			return (16);
		if (c == '.')
			return (17);
		return (-1);
	}

	private boolean checkForNumber(String token, Scanner line)
	{
		int transitions[][] = {
			{ 1, 5, 5, 5, 5, 5, 5, 5, 5, 5, 8, 8, 8, 8, 8, 8, 8, 8},
			{ 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 8, 2, 8, 8, 8, 8, 4, 8},
			{ 2, 2, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8},
			{ 3, 3, 3, 3, 3, 3, 3, 3, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8},
			{ 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 8, 8},
			{ 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 8, 8, 8, 8, 8, 8, 8, 6},
			{ 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 8, 8, 8, 8, 8, 7, 8, 8},
			{ 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8}
		};
		int q = 0;
		int i = -1;
		while (++i < token.length() && q != 8)
		{
			int c = charToInt(token.charAt(i));
			if (c == -1)
				return false;
			q = transitions[q][c];
		}
		Lex lex;
		switch (q)
		{
			case 2:
				lex = new Lex(token, Type.BINARY);
				printLex(lex);
				return true;
			case 3:
				lex = new Lex(token, Type.OCTAL);
				printLex(lex);
				return true;
			case 4:
				lex = new Lex(token, Type.HEXADECIMAL);
				printLex(lex);
				return true;
			case 5:
				lex = new Lex(token, Type.DECIMAL);
				printLex(lex);
				return true;
			case 6:
				lex = new Lex(token, Type.DOUBLE);
				printLex(lex);
				return true;
			case 7:
				lex = new Lex(token, Type.FLOAT);
				printLex(lex);
				return true;
			default:
				return false;
		}
	}

	private boolean checkForString(String token, Scanner line)
	{
		if (token.length() >= 2 && token.charAt(0) == '"' && token.charAt(token.length() - 1) == '"')
		{
			for (int i = 1; i < token.length() - 1; i++)
				if (token.charAt(i) == '"' && token.charAt(i - 1) != '\\')
					return false;
			Lex lex = new Lex(token, Type.STRING);
			printLex(lex);
			return true;
		}
		return false;
	}

	private boolean checkForComment(String token, Scanner line)
	{
		if (token.length() >= 2 && token.charAt(0) == '\\' && token.charAt(1) == '\\')
		{
			Lex lex = new Lex(token, Type.COMMENT);
			while (line.hasNext())
			{
				lex.value = lex.value.concat(" ".concat(line.next()));
			}
			printLex(lex);
			return true;
		}
		return false;
	}

	private boolean checkForIdentifier(String token, Scanner line)
	{
		int i = -1;
		while (++i < token.length())
			if (!(Character.isLetterOrDigit(token.charAt(i)) || token.charAt(i) == '_'))
				return false;
		Lex lex = new Lex(token, Type.IDENTIFIER);
		printLex(lex);
		return true;
	}

	private boolean identifyToken(String token, Scanner line)
	{
		return (checkForOperator(token, line) || checkForComment(token, line) ||
				checkForReserved(token, line) || checkForChar(token, line) ||
				checkForString(token, line) || checkForNumber(token, line) ||
				checkForPreprocessor(token, line) || checkForIdentifier(token, line) ||
				checkForBracket(token, line));
	}

	public void run(String[] args)
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

				while(sc.hasNext())
				{
					Scanner t = new Scanner(sc.next()).useDelimiter("\\s+");

					while(t.hasNext())
					{
						String cur = t.next();
						boolean separator = cur.charAt(cur.length() - 1) == ';';
						if (separator)
							cur = cur.substring(0, cur.length() - 1);

						if (!identifyToken(cur, t))
						{
							Lex lex = new Lex(cur, Type.ERROR);
							printLex(lex);
						}
						if (separator)
						{
							Lex lex = new Lex(";", Type.SEPARATOR);
							printLex(lex);
						}
					}
					System.out.printf("\n");
				}
			}
			catch(FileNotFoundException ex)
			{
				System.out.println("File not found");
			}
			/*catch(Exception ex)
			{
				System.out.println("Error");
			}*/
		}
	}
};

class Main
{
	public static void main(String[] args)
	{
		Program pr = new Program();
		pr.run(args);
	}
}