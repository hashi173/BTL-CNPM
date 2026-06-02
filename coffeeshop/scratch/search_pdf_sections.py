import sys
import PyPDF2

sys.stdout.reconfigure(encoding='utf-8')

pdf_path = 'd:/Project/BTL-CNPM/BG CNPM 2020.pdf'
reader = PyPDF2.PdfReader(pdf_path)

# Let's search for the exact pages of 9.5.2 and 9.5.3
for idx, page in enumerate(reader.pages):
    text = page.extract_text()
    if '9.5.2' in text:
        print(f"9.5.2 found on Page {idx+1}")
    if '9.5.3' in text:
        print(f"9.5.3 found on Page {idx+1}")
